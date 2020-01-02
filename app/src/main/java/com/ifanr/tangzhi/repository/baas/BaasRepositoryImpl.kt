package com.ifanr.tangzhi.repository.baas

import androidx.paging.PagedList
import com.google.gson.reflect.TypeToken
import com.ifanr.tangzhi.ext.*
import com.ifanr.tangzhi.model.*
import com.ifanr.tangzhi.repository.baas.datasource.ProductListDataSource
import com.ifanr.tangzhi.repository.baas.datasource.SearchDataSource
import com.ifanr.tangzhi.ui.widgets.CommentSwitch
import com.ifanr.tangzhi.util.uuid
import com.minapp.android.sdk.Global
import com.minapp.android.sdk.auth.Auth
import com.minapp.android.sdk.database.Record
import com.minapp.android.sdk.database.query.Query
import com.minapp.android.sdk.database.query.Where
import io.reactivex.Completable
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class BaasRepositoryImpl @Inject constructor(): BaasRepository {

    private val searchList = mutableListOf<String>().apply {
        addAll(listOf("手机", "果粉 Boys", "VLog 神器", "iPhone XS", "华为的全面屏超广角手机", "听歌响"))
    }

    override fun searchHotKeys(): Single<List<SearchKey>> =
        setting.getValue("hot_search_key", object: TypeToken<List<SearchKey>>(){}.type)

    override fun searchHint(key: String): Single<List<Product>> = product.query<Product>(
        page = 0,
        pageSize = 10,
        query = Query().apply {
            var where = Where.or(
                Where().apply {
                    contains(Product.COL_NAME, key)
                },
                Where().apply {
                    contains(Product.COL_BRIEF, key)
                })
            where = Where.or(where, Where().apply {
                contains(Product.COL_DESCRIPTION, key)
            })
            where = Where.or(where, Where().apply {
                arrayContains(Product.COL_CATEGORY, listOf(key))
            })
            where = Where.and(where, Where().apply {
                equalTo(Product.COL_TYPE, Product.TYPE_HARDWARE)
                equalTo(Product.COL_STATUS, BaseModel.STATUS_APPROVED)
            })
            put(where)
            orderBy(listOf("-${Product.COL_REVIEW_COUNT}",
                "-${Record.UPDATED_AT}",
                "-${Product.COL_PRIORITY}",
                "-${Product.COL_RATING}"))
        }
    ).map { it.data }

    override fun search(key: String): Single<PagedList<Product>> = Single.fromCallable {
        pagedList(
            SearchDataSource(
                key
            )
        )
    }

    override fun loadSearchLog(): Single<List<SearchLog>> = searchLog.query<SearchLog>(
        page = 0,
        pageSize = 100,
        where = Where().apply {
            equalTo(SearchLog.COL_STATUS, BaseModel.STATUS_APPROVED)
        }
    ).map { it.data }

        /*Single.just(searchList.map { SearchLog(key = it) }).delay(1000, TimeUnit.MILLISECONDS)*/

    override fun addSearchLog(key: String): Completable = Completable.fromCallable {
        searchLog.createRecord().apply {
            put(SearchLog.COL_KEY, key)
            put(SearchLog.COL_STATUS, BaseModel.STATUS_APPROVED)
        }.save()
    }

        /*Completable.fromCallable { searchList.add(key) }.delay(1000, TimeUnit.MILLISECONDS)*/

    override fun cleanSearchLog(): Completable = Completable.fromCallable {
        val query = Query().put(Where().apply {
            equalTo(SearchLog.COL_STATUS, BaseModel.STATUS_APPROVED)
        })
        val update = searchLog.createRecord().apply {
            put(SearchLog.COL_STATUS, BaseModel.STATUS_DELETED)
        }
        searchLog.batchUpdate(query, update)
    }

        /*Completable.fromCallable { searchList.clear() }.delay(1000, TimeUnit.MILLISECONDS)*/

    override fun latestProduct(): Single<List<Product>> = product.query<Product>(
        page = 0,
        pageSize = 20,
        where = Where().apply {
            equalTo(Product.COL_TYPE, Product.TYPE_HARDWARE)
            equalTo(Product.COL_STATUS, BaseModel.STATUS_APPROVED)
        },
        query = Query().apply {
            orderBy("-${Record.CREATED_AT}")
        }
    ).map { it.data }

    override fun loadPagedChildComment(
        productId: String,
        reviewId: String,
        parentId: String,
        offset: Int
    ): Single<PageByOffset<Comment>> = productComment.queryByOffset(
        offset = offset,
        where = Where().apply {
            equalTo(Comment.COL_PRODUCT, productId)
            equalTo(Comment.COL_ROOT_ID, reviewId)
            equalTo(Comment.COL_TYPE, Comment.TYPE_COMMENT)
            equalTo(Comment.COL_STATUS, BaseModel.STATUS_APPROVED)
            equalTo(Comment.COL_PARENT_ID, parentId)
        },
        query = Query().apply {
            orderBy("-${Comment.COL_UPVOTE},-${Record.CREATED_AT}")
        }
    )

        /*productComment.queryByOffset(
            offset = offset,
            where = Where().apply {
                equalTo(Comment.COL_PRODUCT, "5dcd168223277b5a7f8bd977")
                equalTo(Comment.COL_TYPE, Comment.TYPE_REVIEW)
                equalTo(Comment.COL_STATUS, BaseModel.STATUS_APPROVED)
            },
            query = Query().apply {
                orderBy("-${Comment.COL_UPVOTE},-${Record.CREATED_AT}")
            }
        )*/


    override fun loadPagedComment(
        productId: String,
        reviewId: String,
        page: Int,
        pageSize: Int
    ): Single<Page<Comment>> = Single.fromCallable {

        // 查询一级评论
        val comments = productComment.query(
            Comment::class.java,
            page = page,
            pageSize = pageSize,
            where = Where().apply {
                equalTo(Comment.COL_PRODUCT, productId)
                equalTo(Comment.COL_ROOT_ID, reviewId)
                equalTo(Comment.COL_TYPE, Comment.TYPE_COMMENT)
                equalTo(Comment.COL_STATUS, BaseModel.STATUS_APPROVED)
                isNull(Comment.COL_PARENT_ID)
            },
            query = Query().apply {
                orderBy("-${Comment.COL_UPVOTE},-${Record.CREATED_AT}")
                expand(Record.CREATED_BY)
            }
        ).blockingGet()

        // 批量拉取二级评论
        if (comments.data.isNotEmpty()) {
            val children = productComment.query(
                Comment::class.java,
                page = 0,
                pageSize = comments.data.size * 10,
                where = Where().apply {
                    equalTo(Comment.COL_PRODUCT, productId)
                    equalTo(Comment.COL_ROOT_ID, reviewId)
                    equalTo(Comment.COL_TYPE, Comment.TYPE_COMMENT)
                    equalTo(Comment.COL_STATUS, BaseModel.STATUS_APPROVED)
                    containedIn(Comment.COL_PARENT_ID, comments.data.map { it.id })
                },
                query = Query().apply {
                    orderBy("-${Comment.COL_UPVOTE},-${Record.CREATED_AT}")
                    expand(listOf(Record.CREATED_BY, Comment.COL_REPLY_TO))
                }
            ).blockingGet().data

            comments.data.forEach {
                children.find { it.parentId == it.id }?.also { firstChild ->
                    it.children = listOf(firstChild)
                }
            }
        }

        comments
    }

    override fun getReviewById(reviewId: String): Single<Comment> =
        productComment.getById(reviewId, expand = listOf(Record.CREATED_BY))

    override fun loadAllTags(productId: String): Single<List<Comment>> = loadPagedComments(
        productId = productId,
        type = Comment.TYPE_TAG,
        pageSize = 999,
        query = Query().apply {
            orderBy("-${Comment.COL_UPVOTE}")
        }
    ).map { it.data }

    override fun loadPagedReviews(
        productId: String,
        page: Int,
        pageSize: Int,
        orderBy: CommentSwitch.Type?
    ): Single<Page<Comment>> = productComment.query(
        page = page,
        pageSize = pageSize,
        where = Where().apply {
            equalTo(Comment.COL_PRODUCT, productId)
            equalTo(Comment.COL_TYPE, Comment.TYPE_REVIEW)
            equalTo(Comment.COL_STATUS, BaseModel.STATUS_APPROVED)
            isNotNull(Comment.COL_CONTENT)
            if (orderBy == CommentSwitch.Type.EDITOR_CHOICE)
                equalTo(Comment.COL_RECOMMENDED, true)
        },
        query = Query().apply {
            expand(Record.CREATED_BY)
            when (orderBy) {
                CommentSwitch.Type.HOTTEST -> orderBy("-${Comment.COL_UPVOTE}")
                CommentSwitch.Type.LATEST -> orderBy("-${Record.CREATED_AT}")
            }
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
        Single.fromCallable { pagedList(dataSource = ProductListDataSource(
            productId
        )
        ) }

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