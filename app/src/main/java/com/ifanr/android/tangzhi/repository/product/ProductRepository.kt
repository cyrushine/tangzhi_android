package com.ifanr.android.tangzhi.repository.product

import com.ifanr.android.tangzhi.Const
import com.ifanr.android.tangzhi.model.Product
import com.ifanr.android.tangzhi.model.ProductList
import com.minapp.android.sdk.util.PagedList
import io.reactivex.Single

interface ProductRepository {

    fun getProductById(id: String): Single<Product>

    /**
     * 分页查询清单列表
     */
    fun getProductListByProductId(
        productId: String,
        page: Int = 0,
        pageSize: Int = Const.PAGE_SIZE
    ): Single<PagedList<ProductList>>

}