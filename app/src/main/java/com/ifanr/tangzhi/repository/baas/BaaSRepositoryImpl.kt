package com.ifanr.tangzhi.repository.baas

import com.ifanr.tangzhi.ext.*
import com.ifanr.tangzhi.model.*
import com.ifanr.tangzhi.repository.*
import com.minapp.android.sdk.auth.Auth
import com.minapp.android.sdk.database.Record
import com.minapp.android.sdk.database.query.Query
import com.minapp.android.sdk.database.query.Where
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class BaaSRepositoryImpl @Inject constructor(): BaaSRepository {

    override fun loadPagedReviews(
        productId: String,
        page: Int,
        pageSize: Int
    ): Single<Page<Comment>> =
        loadPagedComments(
            productId = productId,
            type = Comment.TYPE_REVIEW,
            page = page,
            pageSize = pageSize,
            query = Query().apply {
                expand(Record.CREATED_BY)
            }
        )


    private fun loadPagedComments (
        productId: String,
        type: String,
        page: Int = 0,
        pageSize: Int = 0,
        query: Query? = null
    ): Single<Page<Comment>> = productComment.query(
        page = page,
        pageSize = pageSize,
        where = Where().apply {
            equalTo(Comment.COL_PRODUCT, productId)
            equalTo(Comment.COL_TYPE, type)
            equalTo(Comment.COL_STATUS, BaseModel.STATUS_APPROVED)
        },
        query = query
    )

    override fun favoriteProduct(productId: String): Completable = Completable.fromCallable {
        favorite.createRecord().apply {
            type = Favorite.TYPE_HARDWARE
            subjectId = productId
        }.save()
    }

    override fun unfavoriteProduct(productId: String): Completable = Completable.fromCallable {
        favorite.fetchWithoutData(productId).delete()
    }

    override fun isProductFavorite(productId: String): Single<Boolean> = Single.fromCallable {
        favorite.query(Query().put(Where()
            .equalTo(Favorite.COL_SUBJECT_ID, productId)
            .equalTo(Record.CREATED_BY, Auth.currentUserWithoutData()?.userId)
        )).totalCount > 0
    }

    override fun productList(productId: String): Single<androidx.paging.PagedList<ProductList>> =
        Single.fromCallable { pagedList(dataSource = ProductListDataSource(productId)) }

    override fun getProductsByIds(ids: List<String>): Single<List<Product>> =
        product.getByIds(ids)

    override fun getProductById(id: String): Single<Product> =
        product.getById(id)

    override fun getProductListByProductId(
        productId: String,
        page: Int,
        pageSize: Int
    ): Single<Page<ProductList>> = itemList.query (
        clz = ProductList::class.java,
        page = page,
        pageSize = pageSize,
        where = Where().apply {
            containedIn(ProductList.COL_ITEMS, listOf(productId))
            equalTo(ProductList.COL_STATUS, BaseModel.STATUS_APPROVED)
        })

    override fun getProductParamsById(paramId: String): Single<ProductParams> =
        productParam.getById(paramId)
}