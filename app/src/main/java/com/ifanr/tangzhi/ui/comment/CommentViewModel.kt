package com.ifanr.tangzhi.ui.comment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.Event
import com.ifanr.tangzhi.EventBus
import com.ifanr.tangzhi.ext.networkJob
import com.ifanr.tangzhi.model.Comment
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import com.ifanr.tangzhi.util.LoadingState
import com.ifanr.tangzhi.util.uuid
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CommentViewModel @Inject constructor(
    private val repository: BaasRepository,
    private val bus: EventBus
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

    /**
     * 第一条是 review
     * 后续的是 comment
     */
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

        bus.subscribe(this, Consumer {
            when (it) {
                is Event.CommentCreated -> {
                    if (it.comment.productId == productId.value &&
                        it.comment.rootId == reviewId.value) {
                        onNewComment(it.comment)
                    }
                }
            }
        })
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
                    val replaced = updated.copy(
                        voted = !voted,
                        upvote = if (voted) updated.upvote - 1 else updated.upvote + 1)

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
        val data = comments.value?.toMutableList()
        if (!data.isNullOrEmpty()) {
            val parentIndex = data.indexOfFirst {
                it.children.find { it.id == indicatorId } != null }
            if (parentIndex > 0) {
                val parent = data[parentIndex]

                // 分页加载二级评论
                repository.loadPagedChildComment(
                    productId = productId.value ?: "",
                    reviewId = reviewId.value ?: "",
                    parentId = parent.id,
                    offset = parent.children.size - 1)
                    .networkJob(vm = this, loading = loading)
                    .subscribe(Consumer {
                        val list = comments.value?.toMutableList()
                        if (!list.isNullOrEmpty()) {
                            val index = list.indexOfFirst { it.id == parent.id }
                            if (index > 0) {
                                list[index] = list[index].copy().apply {
                                    children = children.toMutableList().apply {
                                        removeAt(lastIndex)
                                        addAll(it.data)
                                        if (it.data.size >= it.limit - it.offset)
                                            add(Comment(id = uuid(), loading = false))
                                    }
                                }
                                comments.value = list
                            }
                        }
                    })
            }
        }
    }

    /**
     * 需要把新评论插入到合适的位置
     */
    private fun onNewComment(new: Comment) {
        val list = comments.value?.toMutableList()
        if (!list.isNullOrEmpty()) {

            // 一级评论
            if (new.parentId.isEmpty()) {
                list.add(1, new)
                list[0] = list[0].copy().apply { replyCount++ }
            } else {

                // 二级评论
                val updateIndex = list.indexOfFirst { it.id == new.parentId }
                if (updateIndex >= 0) {
                    list[updateIndex] = list[updateIndex].copy().apply {
                        replyCount++
                        children = children.toMutableList().apply { add(0, new) }
                    }
                }
            }
            comments.value = list
        }
    }
}