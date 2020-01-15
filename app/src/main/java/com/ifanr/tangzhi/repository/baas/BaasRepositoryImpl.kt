package com.ifanr.tangzhi.repository.baas

import android.content.Context
import androidx.paging.PagedList
import com.google.gson.reflect.TypeToken
import com.ifanr.tangzhi.Const
import com.ifanr.tangzhi.Event
import com.ifanr.tangzhi.EventBus
import com.ifanr.tangzhi.exceptions.NeedSignInException
import com.ifanr.tangzhi.ext.*
import com.ifanr.tangzhi.model.*
import com.ifanr.tangzhi.repository.baas.datasource.PointLogDataSource
import com.ifanr.tangzhi.repository.baas.datasource.ProductListDataSource
import com.ifanr.tangzhi.repository.baas.datasource.SearchDataSource
import com.ifanr.tangzhi.ui.widgets.CommentSwitch
import com.minapp.android.sdk.auth.Auth
import com.minapp.android.sdk.database.Record
import com.minapp.android.sdk.database.query.Query
import com.minapp.android.sdk.database.query.Where
import com.minapp.android.sdk.storage.CloudFile
import com.minapp.android.sdk.storage.Storage
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import javax.inject.Inject

class BaasRepositoryImpl @Inject constructor(
    private val bus: EventBus,
    private val ctx: Context
): BaasRepository {

    private var cachedUserBanners: List<String>? = null
    private val cachedUserBannersLock = ReentrantLock(true)


    init {
        cachedUserBanners()
            .delay(10 * 1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }


    override fun loadCommentVotes(ids: List<String>): Single<List<VoteLog>> =
        Single.fromCallable {
            val userId = userId()
            if (userId.isNullOrEmpty() || ids.isEmpty())
                return@fromCallable emptyList<VoteLog>()

            val where = Where().apply {
                equalTo(Record.CREATED_BY, userId.toLong())
                equalTo(VoteLog.COL_TYPE, VoteLog.TYPE_COMMENT)
                equalTo(VoteLog.COL_IS_POSITIVE, true)
                containedIn(VoteLog.COL_SUBJECT_ID, ids)
            }
            voteLog.query(VoteLog::class.java, page = 0, pageSize = ids.size, where = where)
                .blockingGet().data
        }

    override fun voteForComment(id: String): Completable = Completable.fromAction {
        assertSignIn()
        if (loadCommentVotes(listOf(id)).blockingGet().isEmpty()) {
            voteLog.createRecord().apply {
                put(VoteLog.COL_TYPE, VoteLog.TYPE_COMMENT)
                put(VoteLog.COL_SUBJECT_ID, id)
                put(VoteLog.COL_IS_POSITIVE, true)
                put(Record.CREATED_BY, userId()?.toLong())
            }.save()
        }
    }

    override fun removeVoteForComment(id: String): Completable = Completable.fromAction {
        val voteId = loadCommentVotes(listOf(id)).blockingGet().firstOrNull()?.id
        if (!voteId.isNullOrEmpty())
            voteLog.fetchWithoutData(voteId).delete()
    }

    override fun isProductFollowed(productId: String): Single<Boolean> = Single.fromCallable {
        val userId = userId()
        if (userId.isNullOrEmpty()) {
            return@fromCallable false
        }

        val where = Where().apply {
            equalTo(Favorite.COL_TYPE, Favorite.TYPE_HARDWARE)
            equalTo(Favorite.COL_ACTION, Favorite.ACTION_FOLLOW)
            equalTo(Favorite.COL_SUBJECT_ID, productId)
            equalTo(Record.CREATED_BY, userId.toLong())
        }
        favorite.count(Query().apply { put(where) }) > 0
    }

    override fun followProduct(productId: String): Completable = Completable.fromAction {
        assertSignIn()
        if (!isProductFollowed(productId).blockingGet()) {
            favorite.createRecord().apply {
                put(Favorite.COL_TYPE, Favorite.TYPE_HARDWARE)
                put(Favorite.COL_ACTION, Favorite.ACTION_FOLLOW)
                put(Favorite.COL_SUBJECT_ID, productId)
            }.save()
        }
    }

    override fun unFollowProduct(productId: String): Completable = Completable.fromAction {
        assertSignIn()
        val where = Where().apply {
            equalTo(Record.CREATED_BY, userId()?.toLong())
            equalTo(Favorite.COL_TYPE, Favorite.TYPE_HARDWARE)
            equalTo(Favorite.COL_ACTION, Favorite.ACTION_FOLLOW)
            equalTo(Favorite.COL_SUBJECT_ID, productId)
        }
        favorite.query(Query().apply { put(where) }).objects?.firstOrNull()?.delete()
    }

    override fun sendReview(
        productId: String,
        productName: String,
        content: String,
        rating: Float,
        images: List<String>
    ): Single<Comment> = Single.fromCallable {
        val imageUrls = images.map {
            try {
                uploadCommentImage(it).blockingGet()
            } catch (e: Exception) {
                throw Exception("上传图片失败($it)", e)
            }
        }
        productComment.createRecord().apply {
            put(Comment.COL_TYPE, Comment.TYPE_REVIEW)
            put(Comment.COL_PRODUCT, productId)
            put(Comment.COL_TITLE, productName)
            put(Comment.COL_CONTENT, content)
            put(Comment.COL_RATING, rating)
            put(Comment.COL_IMAGE, imageUrls.toTypedArray())
        }.save().let { Comment(it) }
    }


    /**
     * 上传评论里的图片
     * @param path file://...
     * @return 服务器路径
     */
    private fun uploadCommentImage(path: String): Single<String> = Single.fromCallable {
        val file = File(path)
        Storage.uploadFile(file.name, "5c80b90a6383972c4a611291", file.readBytes()).path
    }



    override fun pointLogList(type: String?): Single<PagedList<PointLog>> = Single.fromCallable {
        val userId = currentUserId() ?: throw NeedSignInException()
        pagedList(dataSource = PointLogDataSource(
            ctx = ctx,
            userId = userId,
            type = type
        ))
    }

    override fun loadPagedPointLog(page: Int, type: String?): Single<Page<PointLog>> {
        val userId = currentUserId()
        return if (userId == null) {
            Single.just(Page())
        } else {
            pointLog.query(
                clz = PointLog::class.java,
                page = page,
                pageSize = Const.PAGE_SIZE,
                where = Where().apply {
                    equalTo(Record.CREATED_BY, userId)
                    if (type != null) {
                        equalTo(PointLog.COL_TYPE, type)
                    }
                }
            ).map {
                it.data.forEach {




                }
                it
            }
        }
    }

    private fun currentUserId(): Long? = Auth.currentUserWithoutData()?.userId


    override fun uploadUserAvatar(fileName: String, data: ByteArray): Single<CloudFile> =
        Single.fromCallable {
            Storage.uploadFile(fileName, "5dce53687e806526fb8b6c2b", data)
        }


    override fun updateProfile(update: UserProfile): Completable = Completable.fromCallable {
        val dao = Auth.currentUserWithoutData() ?: throw NeedSignInException()
        dao.put(UserProfile.COL_DISPLAY_AVATAR, update.displayAvatar)
        dao.put(UserProfile.COL_DISPLAY_NAME, update.displayName)
        dao.put(UserProfile.COL_MOTTO, update.motto)
        dao.put(UserProfile.COL_PROFESSION, update.profession)
        dao.put(UserProfile.COL_PHONE, update.phone)
        dao.put(UserProfile.COL_BANNER, update.banner)
        dao.save()
        bus.post(Event.ProfileChanged)
    }



    override fun signOut() {
        Auth.logout()
        bus.post(Event.SignOut)
    }



    override fun signedIn(): Boolean =
        Auth.signedIn()



    override fun loadUserProfile(): Single<UserProfile> = Single.fromCallable {
        val user = Auth.currentUser()
        if (user != null)
            UserProfile(user)
        else
            null
    }



    override fun cachedUserBanners(): Single<List<String>> = Single.fromCallable {
        if (cachedUserBanners == null) {
            cachedUserBannersLock.lock()
            try {
                if (cachedUserBanners == null) {
                    cachedUserBanners = setting.getValue<List<String>>(
                        "user_banner", object: TypeToken<List<String>>(){}.type).blockingGet()
                }
            }
            catch (e: Exception) { throw e }
            finally { cachedUserBannersLock.unlock() }
        }
        cachedUserBanners
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
        addSearchLog(key).subscribeOn(Schedulers.io()).subscribe()
        pagedList(
            SearchDataSource(
                key
            )
        )
    }


    override fun loadSearchLog(): Single<List<SearchLog>> {
        val id = currentUserId()
        return if (id != null) {
            searchLog.query<SearchLog>(
                page = 0,
                pageSize = 100,
                where = Where().apply {
                    equalTo(SearchLog.COL_STATUS, BaseModel.STATUS_APPROVED)
                    equalTo(Record.CREATED_BY, id)
                }
            ).map { it.data }
        } else {
            Single.just(emptyList())
        }
    }



    /**
     * 增加一条搜索历史
     */
    private fun addSearchLog(key: String): Completable = Completable.fromCallable {
        val id = currentUserId()
        if (id != null) {
            val exist = searchLog.count(Query().put(Where().apply {
                equalTo(Record.CREATED_BY, id)
                equalTo(SearchLog.COL_KEY, key)
                equalTo(SearchLog.COL_STATUS, BaseModel.STATUS_APPROVED)
            })) > 0
            if (!exist) {
                searchLog.createRecord().apply {
                    put(SearchLog.COL_KEY, key)
                    put(SearchLog.COL_STATUS, BaseModel.STATUS_APPROVED)
                }.save()
            }
        }
    }



    override fun cleanSearchLog(): Completable = Completable.fromCallable {
        val userId = currentUserId()
        if (userId != null) {
            val query = Query().put(Where().apply {
                equalTo(SearchLog.COL_STATUS, BaseModel.STATUS_APPROVED)
                equalTo(Record.CREATED_BY, userId)
            })
            val update = searchLog.createRecord().apply {
                put(SearchLog.COL_STATUS, BaseModel.STATUS_DELETED)
            }
            searchLog.batchUpdate(query, update)
        }
    }




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
            orderBy("-${Comment.COL_UPVOTE}", "-${Record.UPDATED_AT}")
        })
        .map { it.data }
        .doOnSuccess {
            // 查询是否点赞
            if (signedIn()) {
                val voted = loadCommentVotes(it.map { it.id }).blockingGet()
                it.forEach { tag ->
                    tag.voted = voted.any { it.subjectId == tag.id && it.isPositive }
                }
            }
        }

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

    override fun productList(productId: String): Single<PagedList<ProductList>> =
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


    @Throws(NeedSignInException::class)
    private fun assertSignIn() {
        if (!signedIn())
            throw NeedSignInException()
    }


}