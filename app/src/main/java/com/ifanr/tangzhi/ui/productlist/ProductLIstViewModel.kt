package com.ifanr.tangzhi.ui.productlist

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.ifanr.tangzhi.model.ProductList
import com.ifanr.tangzhi.repository.product.ProductRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProductLIstViewModel @Inject constructor(
    private val repository: ProductRepository
): BaseViewModel() {

    val list = MutableLiveData<PagedList<ProductList>>()

    fun load(productId: String) {
        repository.productList(productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(this)
            .subscribe(Consumer { list.value = it })
    }

}