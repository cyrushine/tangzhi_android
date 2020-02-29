package com.ifanr.tangzhi.repository.baas

import android.content.Context
import android.util.Log
import androidx.paging.PagedList
import com.google.gson.reflect.TypeToken
import com.ifanr.tangzhi.Const
import com.ifanr.tangzhi.Event
import com.ifanr.tangzhi.EventBus
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.exceptions.BaaSException
import com.ifanr.tangzhi.exceptions.NeedSignInException
import com.ifanr.tangzhi.exceptions.UniqueRuleException
import com.ifanr.tangzhi.ext.*
import com.ifanr.tangzhi.model.*
import com.ifanr.tangzhi.repository.baas.datasource.*
import com.ifanr.tangzhi.ui.widgets.CommentSwitch
import com.ifanr.tangzhi.util.AppGson
import com.ifanr.tangzhi.util.uuid
import com.minapp.android.sdk.BaaS
import com.minapp.android.sdk.auth.Auth
import com.minapp.android.sdk.auth.model.SignInByPhoneRequest
import com.minapp.android.sdk.auth.model.UpdateUserReq
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

    companion object {
        private const val TAG = "BaasRepositoryImpl"
    }

    private var cachedUserBanners: List<String>? = null
    private val cachedUserBannersLock = ReentrantLock(true)

    // 标签颜色列表
    private val productTagThemes = listOf(
        "#FDEDEC", "#F4ECF6", "#EBF5FA", "#E8F8F5", "#EAF7EF", "#FEF5E8", "#FBEEE7", "#F2F4F4",
        "#F9DBD9", "#E7DBEE", "#D6EAF7", "#D2F2EB", "#D5EEE0", "#FDEAD2", "#F6DDCE", "#E5E8E8",
        "#F7F5DD", "#ECE9D2", "#F9F1D4", "#F2EBE6", "#EFEFF5", "#EFF1EB", "#EFECF1", "#EDF4F8")


    init {
        cachedUserBanners()
            .delay(10 * 1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }


    override fun getProductReviewCount(productId: String): Single<Long> = Single.fromCallable {
        var where = Where().apply {
            equalTo(Comment.COL_PRODUCT, productId)
            equalTo(Comment.COL_TYPE, Comment.TYPE_REVIEW)
        }

        // 自己可以看到待审核的内容
        if (signedIn()) {
            where = Where.and(
                where,
                Where.or(
                    Where().apply {
                        equalTo(Record.CREATED_BY, userId()?.toLong())
                        equalTo(Comment.COL_STATUS, BaseModel.STATUS_PENDING)
                    },
                    Where().apply {
                        equalTo(Comment.COL_STATUS, BaseModel.STATUS_APPROVED)
                    }
                )
            )
        } else {
            where = Where.and(
                where,
                Where().apply {
                    equalTo(Comment.COL_STATUS, BaseModel.STATUS_APPROVED)
                }
            )
        }

        val query = Query().apply {
            put(where)
        }
        Tables.comment.count(query).toLong()
    }

    override fun reportComment(id: String): Completable = Completable.fromAction {
        assertSignIn()
        val resp = BaaS.invokeCloudFunc("reportComment", id, true)
        if (resp.code != 0) {
            val errMsg = runCatching { AppGson.toJson(resp.error) }.getOrNull()
            throw Exception("invoke baas cloud func fail, reportComment($id), $errMsg")
        }
    }

    override fun relatedProducts(ids: List<String>): Single<List<Product>> =
        Tables.product.query(
            clz = Product::class.java,
            page = -1,
            where = Where().apply {
                containedIn(Record.ID, ids)
                equalTo(Product.COL_STATUS, BaseModel.STATUS_APPROVED)
                equalTo(Product.COL_TYPE, Product.TYPE_HARDWARE)
            }
        ).map { it.data }

    override fun timelineList(): Single<PagedList<Timeline>> = Single.fromCallable {
        assertSignIn()
        pagedList(dataSource = TimelineDataSource(this))
    }

    override fun followsList(): Single<PagedList<Product>> = Single.fromCallable {
        assertSignIn()
        pagedList(dataSource = FollowsDataSource(this))
    }

    override fun systemMessageList(): Single<PagedList<Message>> = Single.fromCallable {
        assertSignIn()
        pagedList(dataSource = SystemMessageDataSource(this))
    }

    override fun messageList(): Single<PagedList<Message>> = Single.fromCallable {
        assertSignIn()
        pagedList(dataSource = MessageDataSource(this))
    }

    override fun verifySmsCode(phone: String, code: String): Completable = Completable.fromAction {
        if (!BaaS.verifySmsCode(phone, code))
            throw Exception(ctx.getString(R.string.incorrect_sms_code))
    }

    override fun currentUserWithoutData(): UserProfile? {
        val currentUser = Auth.currentUserWithoutData()
        return if (currentUser != null) UserProfile(user = currentUser) else currentUser
    }

    override fun signInAnonymous(): Completable = Completable.fromAction {
        Auth.signInAnonymous()
    }

    override fun updateUserPhone(phone: String): Completable = Completable.fromAction {
        val currentUser = Auth.currentUser() ?: throw NeedSignInException()
        currentUser.updateUser(UpdateUserReq().apply {
            this.phone = phone
        })
        currentUser.put(UserProfile.COL_PHONE, phone)
        currentUser.save()
        bus.post(Event.UserPhoneChanged(phone = phone))
    }

    override fun signInByEmail(email: String, pwd: String): Completable = Completable.fromAction {
        Auth.signInByEmail(email, pwd)
    }

    override fun signInByPhone(phone: String, smsCode: String): Completable = Completable.fromAction {
        Auth.signInByPhone(SignInByPhoneRequest(phone, smsCode))
    }

    override fun sendSmsCode(phone: String): Completable = Completable.fromAction {
        if (!BaaS.sendSmsCode(phone))
            throw BaaSException("status != ok")
    }

    /**
     * 通用的查询 [Tables.comment] 表的方法
     * 增加了点赞字段的支持
     */
    private fun queryProductComment(
        page: Int = 0,
        pageSize: Int = Const.PAGE_SIZE,
        where: Where? = null,
        query: Query? = null
    ): Single<Page<Comment>> {

        // 自己可以看到待审核的内容
        val w: Where
        if (signedIn()) {
            w = Where.and(
                where,
                Where.or(
                    Where().apply {
                        equalTo(Record.CREATED_BY, userId()?.toLong())
                        equalTo(Comment.COL_STATUS, BaseModel.STATUS_PENDING)
                    },
                    Where().apply {
                        equalTo(Comment.COL_STATUS, BaseModel.STATUS_APPROVED)
                    }
                )
            )
        } else {
            w = Where.and(
                where,
                Where().apply {
                    equalTo(Comment.COL_STATUS, BaseModel.STATUS_APPROVED)
                }
            )
        }

        return Tables.comment.query(
            clz = Comment::class.java,
            page = page,
            pageSize = pageSize,
            where = w,
            query = query
        ).doOnSuccess { setVoteProperty(it.data) }
    }



    override fun myProductReview(productId: String): Single<Comment> = Single.fromCallable {
        assertSignIn()
        queryProductComment(
            page = 0,
            pageSize = 1,
            where = Where().apply {
                equalTo(Comment.COL_TYPE, Comment.TYPE_REVIEW)
                equalTo(Comment.COL_PRODUCT, productId)
                equalTo(Record.CREATED_BY, userId()?.toLong())
                notEqualTo(Comment.COL_STATUS, BaseModel.STATUS_DELETED)
            }
        ).blockingGet().data.first()
    }

    override fun sendComment(
        productId: String,
        rootId: String,
        content: String,
        parentId: String?,
        replyId: String?,
        replyTo: Long?
    ): Single<Comment> = Single.fromCallable {
        assertSignIn()
        productId.assertNotEmpty("productId")
        rootId.assertNotEmpty("rootId")
        content.assertNotEmpty("content")

        val comment = Tables.comment.createRecord().apply {
            put(Comment.COL_TYPE, Comment.TYPE_COMMENT)
            put(Comment.COL_PRODUCT, productId)
            put(Comment.COL_ROOT_ID, rootId)
            put(Comment.COL_CONTENT, content)
            if (!parentId.isNullOrEmpty()) {
                put(Comment.COL_PARENT_ID, parentId)
            }
            if (!replyId.isNullOrEmpty()) {
                put(Comment.COL_REPLY_ID, replyId)
            }
            if (replyTo != null && replyTo > 0) {
                put(Comment.COL_REPLY_TO, replyTo)
            }
        }.save().let {
            Tables.comment.getById<Comment>(it.id!!,
                listOf(Record.CREATED_BY, Comment.COL_REPLY_TO)).blockingGet()!!
        }
        bus.post(Event.CommentCreated(comment))
        comment
    }

    override fun isProductTagExist(productId: String, content: String): Single<Boolean> =
        Single.fromCallable {
            Tables.comment.count(Query().put(Where().apply {
                equalTo(Comment.COL_TYPE, Comment.TYPE_TAG)
                equalTo(Comment.COL_PRODUCT, productId)
                equalTo(Comment.COL_CONTENT, content)
                equalTo(Comment.COL_CREATED_BY_USER, true)
            })) > 0
        }

    override fun createProductTag(productId: String, content: String): Single<Comment> = Single.fromCallable {
        assertSignIn()
        val safeContent = content.trim()
        if (safeContent.isEmpty())
            throw IllegalStateException("标签不能为空")

        if (isProductTagExist(productId = productId, content = safeContent).blockingGet())
            throw UniqueRuleException("产品标签「$safeContent」已存在")

        Tables.comment.createRecord().apply {
            put(Comment.COL_TYPE, Comment.TYPE_TAG)
            put(Comment.COL_PRODUCT, productId)
            put(Comment.COL_CONTENT, safeContent)
            put(Comment.COL_THEME, productTagThemes.random())
            put(Comment.COL_CREATED_BY_USER, true)
        }.save().let { Comment(it) }
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
            Tables.voteLog.query(VoteLog::class.java, page = 0, pageSize = ids.size, where = where)
                .blockingGet().data
        }

    override fun voteForComment(id: String): Completable = Completable.fromAction {
        assertSignIn()
        if (loadCommentVotes(listOf(id)).blockingGet().isEmpty()) {
            Tables.voteLog.createRecord().apply {
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
            Tables.voteLog.fetchWithoutData(voteId).delete()
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
        Tables.favorite.count(Query().apply { put(where) }) > 0
    }

    override fun followProduct(productId: String): Completable = Completable.fromAction {
        assertSignIn()
        if (!isProductFollowed(productId).blockingGet()) {
            Tables.favorite.createRecord().apply {
                put(Favorite.COL_TYPE, Favorite.TYPE_HARDWARE)
                put(Favorite.COL_ACTION, Favorite.ACTION_FOLLOW)
                put(Favorite.COL_SUBJECT_ID, productId)
            }.save()
            bus.post(Event.FollowEvent(productId = productId, follow = true))
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
        Tables.favorite.query(Query().apply { put(where) }).objects?.firstOrNull()?.delete()
        bus.post(Event.FollowEvent(productId = productId, follow = false))
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
                if (it.startsWith(prefix = "/", ignoreCase = true))
                    uploadCommentImage(it).blockingGet()
                else
                    it
            } catch (e: Exception) {
                throw Exception("上传图片失败($it)", e)
            }
        }

        val existing = runCatching { myProductReview(productId).blockingGet() }.getOrNull()

        // 创建一条新点评
        if (existing == null) {
            val create = Tables.comment.createRecord().apply {
                put(Comment.COL_TYPE, Comment.TYPE_REVIEW)
                put(Comment.COL_PRODUCT, productId)
                put(Comment.COL_TITLE, productName)
                put(Comment.COL_CONTENT, content)
                put(Comment.COL_RATING, rating)
                put(Comment.COL_IMAGE, imageUrls.toTypedArray())
            }.save().let { Comment(it) }
            bus.post(Event.ReviewCreated(create))
            create
        } else {

            // 更新点评
            Tables.comment.fetchWithoutData(existing.id).apply {
                put(Comment.COL_CONTENT, content)
                put(Comment.COL_RATING, rating)
                put(Comment.COL_IMAGE, imageUrls.toTypedArray())
            }.save()

            existing.images = images
            existing.content = content
            existing.rating = rating
            bus.post(Event.ReviewChanged(existing))
            existing
        }
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
            Tables.pointLog.query(
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
                    cachedUserBanners = Tables.setting.getValue<List<String>>(
                        "user_banner", object: TypeToken<List<String>>(){}.type).blockingGet()
                }
            }
            catch (e: Exception) { throw e }
            finally { cachedUserBannersLock.unlock() }
        }
        cachedUserBanners
    }



    override fun searchHotKeys(): Single<List<SearchKey>> =
        Tables.setting.getValue("hot_search_key", object: TypeToken<List<SearchKey>>(){}.type)



    override fun searchHint(key: String): Single<List<Product>> = Tables.product.query<Product>(
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
            Tables.searchLog.query<SearchLog>(
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
            val exist = Tables.searchLog.count(Query().put(Where().apply {
                equalTo(Record.CREATED_BY, id)
                equalTo(SearchLog.COL_KEY, key)
                equalTo(SearchLog.COL_STATUS, BaseModel.STATUS_APPROVED)
            })) > 0
            if (!exist) {
                Tables.searchLog.createRecord().apply {
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
            val update = Tables.searchLog.createRecord().apply {
                put(SearchLog.COL_STATUS, BaseModel.STATUS_DELETED)
            }
            Tables.searchLog.batchUpdate(query, update)
        }
    }




    override fun latestProduct(): Single<List<Product>> = Tables.product.query<Product>(
        page = 0,
        pageSize = 20,
        where = Where().apply {
            equalTo(Product.COL_TYPE, Product.TYPE_HARDWARE)
            equalTo(Product.COL_STATUS, BaseModel.STATUS_APPROVED)
        },
        query = Query().apply {
            orderBy("-${Product.COL_RELEASED_AT}")
        }
    ).map { it.data }

    override fun loadPagedChildComment(
        productId: String,
        reviewId: String,
        parentId: String,
        offset: Int
    ): Single<PageByOffset<Comment>> {

        val baseCondition = Where().apply {
            equalTo(Comment.COL_PRODUCT, productId)
            equalTo(Comment.COL_ROOT_ID, reviewId)
            equalTo(Comment.COL_TYPE, Comment.TYPE_COMMENT)
            equalTo(Comment.COL_PARENT_ID, parentId)
        }

        val profileCondition = if (signedIn())
            Where.or(
                Where().apply {
                    equalTo(Comment.COL_STATUS, BaseModel.STATUS_PENDING)
                    equalTo(Record.CREATED_BY, currentUserId())
                },
                Where().apply { equalTo(Comment.COL_STATUS, BaseModel.STATUS_APPROVED) }
            )
        else
            Where().apply { equalTo(Comment.COL_STATUS, BaseModel.STATUS_APPROVED) }

        return Tables.comment.queryByOffset(
            clz = Comment::class.java,
            offset = offset,
            where = Where.and(baseCondition, profileCondition),
            query = Query().apply {
                orderBy(Record.CREATED_AT)
                expand(listOf(Comment.COL_REPLY_TO, Record.CREATED_BY))
            })
            .doOnSuccess { setVoteProperty(it.data) }
    }


    override fun loadPagedComment(
        productId: String,
        reviewId: String,
        page: Int,
        pageSize: Int
    ): Single<Page<Comment>> = Single.fromCallable {

        // 查询一级评论
        val commonCondition = Where().apply {
            equalTo(Comment.COL_PRODUCT, productId)
            equalTo(Comment.COL_ROOT_ID, reviewId)
            equalTo(Comment.COL_TYPE, Comment.TYPE_COMMENT)
            isNull(Comment.COL_PARENT_ID)
        }
        var signInCondition = Where().apply {
            equalTo(Comment.COL_STATUS, BaseModel.STATUS_APPROVED)
        }
        if (signedIn()) {
            signInCondition = Where.or(signInCondition, Where().apply {
                equalTo(Record.CREATED_BY, userId()?.toLong())
                notEqualTo(Comment.COL_STATUS, BaseModel.STATUS_DELETED)
            })
        }
        val comments = Tables.comment.query(
            Comment::class.java,
            page = page,
            pageSize = pageSize,
            where = Where.and(commonCondition, signInCondition),
            query = Query().apply {
                orderBy("-${Comment.COL_UPVOTE},-${Record.CREATED_AT}")
                expand(Record.CREATED_BY)
            }
        ).blockingGet()

        // 批量拉取二级评论
        if (comments.data.isNotEmpty()) {
            val baseCondition = Where().apply {
                equalTo(Comment.COL_PRODUCT, productId)
                equalTo(Comment.COL_ROOT_ID, reviewId)
                equalTo(Comment.COL_TYPE, Comment.TYPE_COMMENT)
                containedIn(Comment.COL_PARENT_ID, comments.data.map { it.id })
            }

            val profileCondition = if (signedIn())
                Where.or(
                    Where().apply {
                        equalTo(Comment.COL_STATUS, BaseModel.STATUS_PENDING)
                        equalTo(Record.CREATED_BY, currentUserId())
                    },
                    Where().apply { equalTo(Comment.COL_STATUS, BaseModel.STATUS_APPROVED) }
                )
            else
                Where().apply { equalTo(Comment.COL_STATUS, BaseModel.STATUS_APPROVED) }

            val children = Tables.comment.query(
                Comment::class.java,
                page = 0,
                pageSize = comments.data.size * 10,
                where = Where.and(baseCondition, profileCondition),
                query = Query().apply {
                    orderBy(Record.CREATED_AT)
                    expand(listOf(Record.CREATED_BY, Comment.COL_REPLY_TO))
                }
            ).blockingGet().data

            comments.data.forEach { parent ->
                children.find { it.parentId == parent.id }?.also { firstChild ->
                    parent.children = listOf(firstChild, Comment(id = uuid(), loading = false))
                }
            }
        }

        comments
    }.doOnSuccess { setVoteProperty(it.data) }

    override fun getReviewById(reviewId: String): Single<Comment> = Single.fromCallable {
        queryProductComment(
            page = 0,
            pageSize = 1,
            where = Where().apply {
                equalTo(Record.ID, reviewId)
            },
            query = Query().apply {
                expand(listOf(Record.CREATED_BY))
            }
        ).blockingGet().data.first()
    }

    override fun loadAllTags(productId: String): Single<List<Comment>> = queryProductComment(
        page = 0,
        pageSize = 999,
        where = Where().apply {
            equalTo(Comment.COL_PRODUCT, productId)
            equalTo(Comment.COL_TYPE, Comment.TYPE_TAG)
        },
        query = Query().apply {
            orderBy("-${Comment.COL_UPVOTE}", "-${Record.UPDATED_AT}")
        })
        .map { it.data }

    override fun loadPagedReviews(
        productId: String,
        page: Int,
        pageSize: Int,
        orderBy: CommentSwitch.Type?
    ): Single<Page<Comment>> = queryProductComment(
        page = page,
        pageSize = pageSize,
        where = Where().apply {
            equalTo(Comment.COL_PRODUCT, productId)
            equalTo(Comment.COL_TYPE, Comment.TYPE_REVIEW)
            isNotNull(Comment.COL_CONTENT)
            notEqualTo(Comment.COL_CONTENT, "")
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

    override fun productList(productId: String): Single<PagedList<ProductList>> =
        Single.fromCallable { pagedList(dataSource = ProductListDataSource(
            productId
        )
        ) }

    override fun getProductsByIds(ids: List<String>): Single<List<Product>> =
        Tables.product.getByIds(ids)

    override fun getProductById(id: String): Single<Product> =
        Tables.product.getById(id)

    override fun getProductListByProductId(
        productId: String,
        page: Int,
        pageSize: Int
    ): Single<Page<ProductList>> = Tables.itemList.query (
        clz = ProductList::class.java,
        page = page,
        pageSize = pageSize,
        where = Where().apply {
            containedIn(ProductList.COL_ITEMS, listOf(productId))
            equalTo(ProductList.COL_STATUS, BaseModel.STATUS_APPROVED)
        })

    override fun getProductParamsById(paramId: String): Single<ProductParams> =
        Tables.productParam.getById(paramId)


    @Throws(NeedSignInException::class)
    private fun assertSignIn() {
        if (!signedIn())
            throw NeedSignInException()
    }


    override fun currentUser(): Single<UserProfile> = Single.fromCallable {
        Auth.currentUser() ?: throw NeedSignInException()
    }.map { UserProfile(user = it) }


    /**
     * 查询并设置评论的点赞状态
     */
    private fun setVoteProperty(comments: List<Comment>) {
        if (signedIn() && comments.isNotEmpty()) {
            try {
                val flatList = comments.flatMap { it.children + it }
                val votes = loadCommentVotes(flatList.map { it.id }).blockingGet()
                flatList.forEach { review ->
                    review.voted = votes.any { it.subjectId == review.id && it.isPositive }
                }
            } catch (e: Exception) {}
        }
    }

}