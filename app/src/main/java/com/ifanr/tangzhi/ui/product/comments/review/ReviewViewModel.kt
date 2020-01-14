package com.ifanr.tangzhi.ui.product.comments.review

import android.util.Log
import androidx.lifecycle.*
import com.ifanr.tangzhi.model.Comment
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import com.ifanr.tangzhi.ui.widgets.CommentSwitch
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ReviewViewModel @Inject constructor (
    private val repository: BaasRepository
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
