package com.ifanr.tangzhi.repository.product

import com.ifanr.tangzhi.Const
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.model.ProductList
import com.ifanr.tangzhi.model.ProductParams
import com.ifanr.tangzhi.ui.productlist.ProductLIstViewModel
import com.minapp.android.sdk.util.PagedList
import io.reactivex.Single

interface ProductRepository {

    fun getProductById(id: String): Single<Product>

    fun getProductsByIds(ids: List<String>): Single<List<Product>>

    fun getProductParamsById(paramId: String): Single<ProductParams>

    /**
     * 分页查询清单列表
     */
    fun getProductListByProductId(
        productId: String,
        page: Int = 0,
        pageSize: Int = Const.PAGE_SIZE
    ): Single<PagedList<ProductList>>

    fun productList(productId: String): androidx.paging.PagedList<ProductList>

}