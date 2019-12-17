package com.ifanr.tangzhi.ui.productlist

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.ifanr.tangzhi.model.ProductList
import com.ifanr.tangzhi.repository.product.ProductRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import javax.inject.Inject

class ProductLIstViewModel @Inject constructor(
    private val repository: ProductRepository
): BaseViewModel() {

    val list = MutableLiveData<PagedList<ProductList>>()

    fun load(productId: String) {
        list.value = repository.productList(productId)
    }

}