package com.ifanr.tangzhi.ui.comment

import androidx.lifecycle.MutableLiveData
import com.ifanr.tangzhi.model.Comment
import com.ifanr.tangzhi.repository.baas.BaaSRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CommentViewModel @Inject constructor(
    private val repository: BaaSRepository
): BaseViewModel() {

    val productId = MutableLiveData<String>()
    val productName = MutableLiveData<String>()
    val reviewId = MutableLiveData<String>()
    private val review = MutableLiveData<Comment>()
    val comments = MutableLiveData<List<Comment>>()

    // 下一页
    private var page = 0

    init {
        reviewId.observeForever { it?.also {
            repository.getReviewById(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDispose(this)
                .subscribe(Consumer { review.value = it })
        }}

        review.observeForever { it?.also { review ->
            val productId = productId.value
            if (!productId.isNullOrEmpty()) {
                repository.loadPagedComment(productId = productId, reviewId = review.id, page = 0)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .autoDispose(this)
                    .subscribe(Consumer {
                        comments.value = listOf(review) + it.data
                        page++
                    })
            }
        }}
    }
}