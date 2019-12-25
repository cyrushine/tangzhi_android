package com.ifanr.tangzhi.repository.baas

import com.ifanr.tangzhi.Const
import com.ifanr.tangzhi.model.*
import io.reactivex.Completable
import io.reactivex.Single

interface BaaSRepository {

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
    ): Single<Page<ProductList>>

    fun productList(productId: String): Single<androidx.paging.PagedList<ProductList>>

    /**
     * 收藏
     */
    fun favoriteProduct(productId: String): Completable

    /**
     * 取消收藏
     */
    fun unfavoriteProduct(productId: String): Completable

    /**
     * 查询是否有收藏
     */
    fun isProductFavorite(productId: String): Single<Boolean>

    /**
     * 点评列表
     */
    fun loadPagedReviews(
        productId: String,
        page: Int = 0,
        pageSize: Int = Const.PAGE_SIZE
    ): Single<Page<Comment>>

    /**
     * 产品的标签列表
     */
    fun loadAllTags(productId: String): Single<List<Comment>>
}