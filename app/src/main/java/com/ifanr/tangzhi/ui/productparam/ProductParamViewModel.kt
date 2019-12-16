package com.ifanr.tangzhi.ui.productparam

import androidx.lifecycle.MutableLiveData
import com.ifanr.tangzhi.repository.product.ProductRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProductParamViewModel @Inject constructor (
    private val repository: ProductRepository
): BaseViewModel() {

    val params = MutableLiveData<Params>()

    val productName = MutableLiveData<String>()

    fun load(productId: String) {
        loadProductParams(productId)
            .subscribeOn(Schedulers.io())
            .autoDispose(this)
            .subscribe(Consumer { params.postValue(it) })
    }

    private fun loadProductParams(productId: String) = Single.fromCallable {
        val product = repository.getProductById(productId).blockingGet()
        productName.postValue(product.name)

        val highlight: List<Params.Param> = if (product.highlightParamVisible)
            product.highlightParam.map { Params.Param(key = it.key, value = it.value) }
        else
            emptyList()

        val all: Map<String, List<Params.Param>> = if (product.paramVisible) {
            repository.getProductParamsById(product.paramId)
                .blockingGet()
                .value
                .map { it.key to it.children
                    .filter { it.display }
                    .map { Params.Param(key = it.key, value = it.value) } }
                .toMap()
        } else {
            emptyMap()
        }

        Params(highlight = highlight, all = all)
    }

}