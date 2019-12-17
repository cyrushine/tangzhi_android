package com.ifanr.tangzhi.repository.product

import com.ifanr.tangzhi.ext.getById
import com.ifanr.tangzhi.ext.getByIds
import com.ifanr.tangzhi.ext.pagedList
import com.ifanr.tangzhi.ext.query
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.model.ProductList
import com.ifanr.tangzhi.model.ProductParams
import com.ifanr.tangzhi.repository.itemList
import com.ifanr.tangzhi.repository.product
import com.ifanr.tangzhi.repository.productParam
import com.ifanr.tangzhi.repository.ProductListDataSource
import com.minapp.android.sdk.database.query.Where
import com.minapp.android.sdk.util.PagedList
import io.reactivex.Single
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(): ProductRepository {

    override fun productList(productId: String): androidx.paging.PagedList<ProductList> = pagedList(
        dataSource = ProductListDataSource(productId)
    )

    override fun getProductsByIds(ids: List<String>): Single<List<Product>> =
        product.getByIds(ids, Product::class.java)

    override fun getProductById(id: String): Single<Product> =
        product.getById(id, Product::class.java)

    override fun getProductListByProductId(
        productId: String,
        page: Int,
        pageSize: Int
    ): Single<PagedList<ProductList>> = itemList.query (
        Where().containedIn(ProductList.COL_ITEMS, listOf(productId))
            .equalTo(ProductList.COL_STATUS, ProductList.STATUS_APPROVED),
        page,
        pageSize,
        ProductList::class.java)

    override fun getProductParamsById(paramId: String): Single<ProductParams> =
        productParam.getById(paramId, ProductParams::class.java)
}