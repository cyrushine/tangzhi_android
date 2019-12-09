package com.ifanr.android.tangzhi.repository.product

import com.ifanr.android.tangzhi.ext.getById
import com.ifanr.android.tangzhi.model.Product
import com.ifanr.android.tangzhi.repository.product
import com.minapp.android.sdk.database.Record
import com.minapp.android.sdk.database.Table
import com.minapp.android.sdk.database.query.Query
import com.minapp.android.sdk.database.query.Where
import io.reactivex.Single
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(): ProductRepository {

    override fun getProductById(id: String): Single<Product> =
        product.getById(id, Product::class.java)


}