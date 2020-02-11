package com.ifanr.tangzhi.ui.product.comments.review

import androidx.lifecycle.*
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.EventBus
import com.ifanr.tangzhi.ext.ioTask
import com.ifanr.tangzhi.model.Comment
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import com.ifanr.tangzhi.ui.widgets.CommentSwitch
import com.ifanr.tangzhi.util.LoadingState
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import kotlin.math.max

class ReviewViewModel @Inject constructor (
    private val repository: BaasRepository,
    private val bus: EventBus
) : BaseViewModel() {

    companion object {
        private const val TAG = "ReviewViewModel"
    }

    enum class State {
        IDLE, LOADING
    }

    // 产品信息
    val product = MutableLiveData<Product>()

    // 标签列表
    val tags = MediatorLiveData<List<Comment>>()

    // 点评列表, boolean == true 表示 scroll to top
    val reviews = MediatorLiveData<Pair<List<Comment>, Boolean>>()

    // 点评总数
    val reviewCount = MutableLiveData<Int>()

    // 排序
    val orderBy = MutableLiveData<CommentSwitch.Type>()

    // 提供给 ProductActivity，发表点评后刷新最新列表
    val refreshToLatest = MutableLiveData<Any>()
    val loading = MutableLiveData<LoadingState>()
    val toast = MutableLiveData<String>()

    // 下一页的页码
    private var page = 0
    private var state = State.IDLE
    // 列表是否结束
    private var end = false

    init {
        tags.addSource(product) {
            it?.id?.also {
                repository.loadAllTags(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .autoDispose(this)
                    .subscribe(Consumer { tags.value = it })
            }
        }
        reviews.addSource(product) {
            refreshReviews()
        }
        reviews.addSource(orderBy) {
            refreshReviews()
        }
    }

    // 给点评点赞
    fun onVoteClick(position: Int) {
        if (!repository.signedIn()) {
            ARouter.getInstance().build(Routes.signInByWechat).navigation()
            return
        }

        val updated = reviews.value?.first?.getOrNull(position)
        if (updated != null) {
            val voted = updated.voted
            val request = if (voted) repository.removeVoteForComment(updated.id)
            else repository.voteForComment(updated.id)

            request.ioTask(this, loadingState = loading)
                .subscribe({

                    // 更新点评列表
                    val list = reviews.value?.first?.toMutableList()
                    if (!list.isNullOrEmpty()) {
                        val updatedIndex = list.indexOfFirst { it.id == updated.id }
                        if (updatedIndex >= 0) {
                            list[updatedIndex] = updated.copy(
                                voted = !voted,
                                upvote = if (voted) updated.upvote - 1 else updated.upvote + 1)
                            reviews.value = list to false
                        }
                    }
                }, { toast.value = it.message })
        }
    }

    /**
     * 刷新点评列表
     */
    private fun refreshReviews() {
        end = false
        page = 0
        tryLoadReviews()
    }

    /**
     * 触发加载下一页
     */
    fun tryLoadNextPage() {
        tryLoadReviews(page + 1)
    }

    /**
     * 「标签」被点击，触发「点赞」or「取消点赞」
     */
    fun onTagClick(position: Int, showLoading: Boolean = true) {
        if (!repository.signedIn()) {
            ARouter.getInstance().build(Routes.signInByWechat).navigation()
            return
        }

        val clicked = tags.value?.getOrNull(position)
        if (clicked != null) {
            val voted = clicked.voted
            val request = if (voted) repository.removeVoteForComment(clicked.id) else
                repository.voteForComment(clicked.id)
            request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    if (showLoading)
                        loading.value = LoadingState.SHOW_DELAY
                }
                .doAfterTerminate {
                    if (showLoading)
                        loading.value = LoadingState.DISMISS
                }
                .autoDispose(this)
                .subscribe({
                    tags.value?.firstOrNull { it.id == clicked.id }?.also {
                        it.upvote = max(it.upvote + (if (voted) -1 else +1), 0)
                        it.voted = !voted
                        tags.value = tags.value
                    }
                }, {
                    toast.value = it.message
                })
        }
    }

    /**
     * 添加标签
     */
    fun addProductTag(content: String): Completable = Completable.fromAction {
        val productId = product.value?.id ?: throw IllegalStateException("productId 不存在")
        val tagCreated = repository.createProductTag(productId, content).blockingGet()
        tags.postValue((tags.value ?: emptyList()) + tagCreated) }
        .doOnError { toast.postValue(it.message) }


    private fun tryLoadReviews(page: Int = 0) {
        val productId = product.value?.id
        val orderBy = this.orderBy.value
        if (state == State.IDLE && !end && !productId.isNullOrEmpty() && orderBy != null) {
            repository.loadPagedReviews(productId = productId, page = page, orderBy = orderBy)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { state = State.LOADING }
                .doAfterTerminate { state = State.IDLE }
                .autoDispose(this)
                .subscribe(Consumer {
                    if (page == 0) {
                        reviews.value = it.data to true
                    } else {
                        val previous = reviews.value?.first
                        val data = if (previous != null) previous + it.data else it.data
                        reviews.value = data to false
                    }
                    if (it.total > reviewCount.value ?: 0)
                        reviewCount.value = it.total

                    if (it.data.size < it.pageSize) {
                        end = true
                    } else {
                        end = false
                        this.page = page
                    }
                })
        }
    }
}
