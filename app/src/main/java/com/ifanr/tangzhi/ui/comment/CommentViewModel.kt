package com.ifanr.tangzhi.ui.comment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.ext.networkJob
import com.ifanr.tangzhi.model.Comment
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import com.ifanr.tangzhi.ui.comment.model.ChildLoadMore
import com.ifanr.tangzhi.util.LoadingState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CommentViewModel @Inject constructor(
    private val repository: BaasRepository
): BaseViewModel() {

    companion object {
        private const val TAG = "CommentViewModel"
    }

    enum class State {
        IDLE, LOADING
    }

    val productId = MutableLiveData<String>()
    val productName = MutableLiveData<String>()
    val reviewId = MutableLiveData<String>()
    val loading = MutableLiveData<LoadingState>()
    val toast = MutableLiveData<String>()
    private val review = MutableLiveData<Comment>()

    // 下一页
    private var page = 0
    private var state = State.IDLE
    // 是否到达叶尾
    private var endList = false
    // 评论列表
    val comments = MutableLiveData<List<Comment>>()

    init {
        reviewId.observeForever { it?.also {
            repository.getReviewById(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDispose(this)
                .subscribe(Consumer {
                    comments.value = listOf(it)
                    review.value = it
                })
        }}

        review.observeForever { tryLoadNextPage() }
    }

    // 点击「有用」
    fun onVoteClick(id: String) {
        if (!repository.signedIn()) {
            ARouter.getInstance().build(Routes.signIn).navigation()
            return
        }

        val updated = comments.value?.flatMap { it.children + it }?.find { it.id == id }
        if (updated != null) {
            val voted = updated.voted
            val request = if (voted) repository.removeVoteForComment(updated.id)
            else repository.voteForComment(updated.id)

            request.networkJob(this, loadingState = loading)
                .subscribe({
                    val replaced = Comment(updated).apply {
                        this.voted = !voted
                        if (voted) upvote-- else upvote++
                    }

                    // 一级评论
                    val list = comments.value?.toMutableList()
                    if (!list.isNullOrEmpty()) {
                        if (updated.parentId.isEmpty()) {
                            val updatedIndex = list.indexOfFirst { it.id == replaced.id }
                            if (updatedIndex >= 0) {
                                list[updatedIndex] = replaced
                                comments.value = list
                            }
                        } else {

                            // 二级评论
                            list.find { it.id == replaced.parentId }?.also { parent ->
                                val updatedIndex = parent.children.indexOfFirst { it.id == replaced.id }
                                if (updatedIndex >= 0) {
                                    parent.children = parent.children.toMutableList().apply {
                                        set(updatedIndex, replaced)
                                    }
                                    comments.value = comments.value
                                }
                            }
                        }
                    }
                }, { toast.value = it.message })
        }
    }

    /**
     * 触发下一页的加载
     */
    fun tryLoadNextPage() {
        val productId = productId.value
        val reviewId = reviewId.value
        if (state == State.IDLE &&
            !endList &&
            !productId.isNullOrEmpty() &&
            !reviewId.isNullOrEmpty()) {

            repository.loadPagedComment(productId = productId, reviewId = reviewId, page = page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { state = State.LOADING }
                .doAfterTerminate { state = State.IDLE }
                .autoDispose(this)
                .subscribe(Consumer {
                    it.data.forEach {
                        if (it.children.size == 1)
                            it.children = it.children + listOf(ChildLoadMore())
                    }
                    comments.value = (comments.value ?: listOf()) + it.data
                    page++

                    if (it.data.size < it.pageSize)
                        endList = true
                })
        }
    }

    /**
     * 加载二级评论的下一页
     * @param indicatorId ChildLoadMore id，用以搜索
     */
    fun loadChild(indicatorId: String) {
        val parent = comments.value?.
            find { it.children.find { it.id == indicatorId } != null }
        if (parent != null) {

            // 分页加载二级评论
            repository.loadPagedChildComment(
                productId = productId.value ?: "",
                reviewId = reviewId.value ?: "",
                parentId = parent.id,
                offset = parent.children.size - 1)
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDispose(this)
                .subscribe(Consumer {
                    parent.children = ArrayList<Comment>().apply {
                        addAll(parent.children.take(parent.children.size - 1))
                        addAll(it.data)
                        if (it.data.size >= it.limit - it.offset)
                            add(ChildLoadMore())
                    }
                    comments.value = comments.value
                })
        }
    }
}