package com.ifanr.tangzhi.ui.relatedproducts

import androidx.lifecycle.MutableLiveData
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.repository.baas.BaaSRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RelatedProductsViewModel @Inject constructor (
    private val repository: BaaSRepository
): BaseViewModel() {

    val products = MutableLiveData<List<Product>>()

    fun load(productId: String) {
        loadRelatedProductsByProductId(productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(this)
            .subscribe(Consumer { products.value = it })
    }

    private fun loadRelatedProductsByProductId(productId: String) = Single.fromCallable {
        val product = repository.getProductById(productId).blockingGet()
        repository.getProductsByIds(product.similarProduct).blockingGet()
    }
}