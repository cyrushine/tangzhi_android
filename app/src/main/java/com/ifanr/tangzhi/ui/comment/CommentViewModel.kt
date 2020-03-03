package com.ifanr.tangzhi.ui.comment

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.RequestManager
import com.ifanr.tangzhi.Event
import com.ifanr.tangzhi.EventBus
import com.ifanr.tangzhi.ext.ioTask
import com.ifanr.tangzhi.ext.shareReview
import com.ifanr.tangzhi.model.Comment
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import com.ifanr.tangzhi.util.LoadingState
import com.ifanr.tangzhi.util.uuid
import com.tencent.mm.opensdk.openapi.IWXAPI
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CommentViewModel @Inject constructor(
    private val repository: BaasRepository,
    private val bus: EventBus,
    private val wxapi: IWXAPI,
    private val requestManager: RequestManager
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


    /**
     * 举报评论
     */
    fun reportComment(id: String) = repository
        .reportComment(id)
        .ioTask(vm = this, loadingState = loading, loadingDelay = false)


    /**
     * 分享评论/点评
     */
    fun shareComment(id: String) {
        ShareCommentTask(id)
            .ioTask(vm = this, loadingState = loading, loadingDelay = false)
            .subscribe({}, { toast.value = "分享失败了(${it.message})" })
    }



    private fun ShareCommentTask(id: String) = Completable.fromAction {
        val comment = comments.value
            ?.flatMap { it.children + it }
            ?.find { it.id == id }
            ?: throw Exception("can't find the comment($id)")

        val reviewId =
            if (comment.type == Comment.TYPE_REVIEW) comment.id else comment.rootId
        if (reviewId.isEmpty())
            throw Exception("review id is empty!")

        // 封面图，取点评的第一张图片 or 产品封面图
        var imgPath: String? = null
        if (comment.type == Comment.TYPE_REVIEW) {
            imgPath = comment.images.firstOrNull()
        } else {
            val review = comments.value?.find { it.id == comment.rootId }
                ?: throw Exception("can't find the review for the comment($id)")
            imgPath = review.images.firstOrNull()
        }
        if (imgPath.isNullOrEmpty()) {
            try {
                imgPath = repository
                    .getProductById(productId.value ?: throw Exception("product id is null!"))
                    .blockingGet()
                    .coverImage
            } catch (e: Exception) {
                throw Exception("receive product cover image fail", e)
            }
        }
        val img: Bitmap
        try {
            img = requestManager.asBitmap().load(imgPath).submit().get()
        } catch (e: Exception) {
            throw Exception("receive product cover image fail", e)
        }

        val content = comment.content

        wxapi.shareReview(reviewId, content, img)
    }



    // 点击「有用」
    fun onVoteClick(id: String) {
        if (!repository.signedIn()) {
            ARouter.getInstance().build(Routes.signInByWechat).navigation()
            return
        }

        val updated = comments.value?.flatMap { it.children + it }?.find { it.id == id }
        if (updated != null) {
            val voted = updated.voted
            val request = if (voted) repository.removeVoteForComment(updated.id)
            else repository.voteForComment(updated.id)

            request.ioTask(this, loadingState = loading)
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
                            val parentIndex = list.indexOfFirst { it.id == replaced.parentId }
                            if (parentIndex >= 0) {
                                val updatedIndex = list[parentIndex].children.indexOfFirst { it.id == replaced.id }
                                if (updatedIndex >= 0) {
                                    val parentReplaced = list[parentIndex].copy().apply {
                                        children = children.toMutableList().apply {
                                            set(updatedIndex, replaced)
                                        }
                                    }
                                    list[parentIndex] = parentReplaced
                                    comments.value = list
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
                    .ioTask(vm = this, loadingState = loading)
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