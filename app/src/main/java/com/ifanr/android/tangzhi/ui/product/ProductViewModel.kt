package com.ifanr.android.tangzhi.ui.product

import androidx.lifecycle.MutableLiveData
import com.ifanr.android.tangzhi.model.Product
import com.ifanr.android.tangzhi.repository.product.ProductRepository
import com.ifanr.android.tangzhi.ui.base.BaseViewModel
import com.ifanr.android.tangzhi.ui.base.autoDispose
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProductViewModel @Inject constructor (
    private val repository: ProductRepository
): BaseViewModel() {

    val product = MutableLiveData<Product>()
    val errorOnLoad = MutableLiveData<Throwable>()

    fun load(productId: String) {
        repository.getProductById(productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(this)
            .subscribe({ product.value = it }, { errorOnLoad.value = it })
    }

}