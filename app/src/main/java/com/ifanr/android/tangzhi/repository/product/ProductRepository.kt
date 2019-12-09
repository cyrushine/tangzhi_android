package com.ifanr.android.tangzhi.repository.product

import com.ifanr.android.tangzhi.model.Product
import io.reactivex.Single

interface ProductRepository {

    fun getProductById(id: String): Single<Product>

}