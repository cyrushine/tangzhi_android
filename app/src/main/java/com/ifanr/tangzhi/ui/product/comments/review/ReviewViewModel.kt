package com.ifanr.tangzhi.ui.product.comments.review

import androidx.lifecycle.*
import com.ifanr.tangzhi.model.Comment
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.repository.baas.BaaSRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ReviewViewModel @Inject constructor (
    private val repository: BaaSRepository
) : BaseViewModel() {

    val product = MutableLiveData<Product>()

    // 标签列表
    val tags = MediatorLiveData<List<Comment>>()


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
    }
}
