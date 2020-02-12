package com.ifanr.tangzhi.repository.baas

import androidx.annotation.FloatRange
import androidx.paging.PagedList
import com.ifanr.tangzhi.Const
import com.ifanr.tangzhi.model.*
import com.ifanr.tangzhi.repository.baas.datasource.MessageDataSource
import com.ifanr.tangzhi.repository.baas.datasource.SystemMessageDataSource
import com.ifanr.tangzhi.ui.widgets.CommentSwitch
import com.minapp.android.sdk.auth.CurrentUser
import com.minapp.android.sdk.storage.CloudFile
import io.reactivex.Completable
import io.reactivex.Single

interface BaasRepository {

    /**
     * 我的动态
     */
    fun timelineList(): Single<PagedList<Timeline>>

    /**
     * 我的关注列表
     */
    fun followsList(): Single<PagedList<Product>>

    /**
     * 我的系统消息列表
     */
    fun systemMessageList(): Single<PagedList<Message>>

    /**
     * 我的消息列表
     */
    fun messageList(): Single<PagedList<Message>>

    /**
     * 检验手机验证码
     */
    fun verifySmsCode(phone: String, code:String): Completable

    /**
     * 当前登录用户
     */
    fun currentUserWithoutData(): UserProfile?

    /**
     * 匿名登录
     */
    fun signInAnonymous(): Completable

    /**
     * 获取当前登录用户
     */
    fun currentUser(): Single<UserProfile>

    /**
     * 更新用户手机号码
     */
    fun updateUserPhone(phone: String): Completable

    /**
     * 邮箱登录
     */
    fun signInByEmail(email: String, pwd: String): Completable

    /**
     * 手机号码登录（注册）
     */
    fun signInByPhone(phone: String, smsCode: String): Completable

    /**
     * 发送手机验证码
     */
    fun sendSmsCode(phone: String): Completable

    /**
     * 我的产品点评
     */
    fun myProductReview(productId: String): Single<Comment>

    /**
     * 发表回复
     * @param rootId 点评 id
     * @param replyId 二级回复才有，回复对象
     * @param parentId 二级回复才有，回复对象所在回复树的跟（一级评论）
     * @param replyTo 回复对象的 userId
     */
    fun sendComment(
        productId: String,
        rootId: String,
        content: String,

        // 以下字段二级回复才需要
        parentId: String? = null,
        replyId: String? = null,
        replyTo: Long? = null
    ): Single<Comment>

    /**
     * 产品标签是否已存在
     */
    fun isProductTagExist(productId: String, content: String): Single<Boolean>

    /**
     * 给产品添加标签
     */
    fun createProductTag(productId: String, content: String): Single<Comment>

    /**
     * 批量查询是否点赞
     * @param ids 评论 id 列表
     * @return 已点赞的评论的记录
     */
    fun loadCommentVotes(ids: List<String>): Single<List<VoteLog>>

    /**
     * 点赞
     */
    fun voteForComment(id: String): Completable

    /**
     * 取消点赞
     */
    fun removeVoteForComment(id: String): Completable

    /**
     * 是否有关注产品
     * @return 未登录的话返回 false
     */
    fun isProductFollowed(productId: String): Single<Boolean>

    /**
     * 关注产品
     */
    fun followProduct(productId: String): Completable

    /**
     * 取消关注产品
     */
    fun unFollowProduct(productId: String): Completable

    /**
     * 发表点评
     * @param images 图片会上传到服务器（如果是 file://，否则认为是网络路径不进行文件上传），然后使用服务器地址
     */
    fun sendReview(
        productId: String,
        productName: String,
        content: String,
        @FloatRange(from = 0.0, to = 10.0) rating: Float,
        images: List<String>
    ): Single<Comment>

    /**
     * 积分记录 PagedList
     */
    fun pointLogList(type: String?): Single<PagedList<PointLog>>

    /**
     * 分页查询积分记录
     */
    fun loadPagedPointLog(page: Int = 0, type: String? = null): Single<Page<PointLog>>

    /**
     * 上传用户自定义的头像
     */
    fun uploadUserAvatar(fileName: String, data: ByteArray): Single<CloudFile>

    /**
     * 更新个人资料
     */
    fun updateProfile(update: UserProfile): Completable

    /**
     * 登出
     */
    fun signOut()

    /**
     * 是否已登录
     */
    fun signedIn(): Boolean

    /**
     * 当前登录的用户信息
     */
    fun loadUserProfile(): Single<UserProfile>

    /**
     * 用户可选 banner 图列表
     */
    fun cachedUserBanners(): Single<List<String>>

    /**
     * 热门搜索列表
     */
    fun searchHotKeys(): Single<List<SearchKey>>

    /**
     * 搜索的 autocomplete
     */
    fun searchHint(key: String): Single<List<Product>>

    /**
     * 搜索
     */
    fun search(key: String): Single<PagedList<Product>>

    /**
     * 加载所有的搜索历史
     */
    fun loadSearchLog(): Single<List<SearchLog>>

    /**
     * 清空搜索历史
     */
    fun cleanSearchLog(): Completable

    /**
     * 最新上线的 20 个产品列表
     */
    fun latestProduct(): Single<List<Product>>

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

    fun productList(productId: String): Single<PagedList<ProductList>>

    /**
     * 点评列表
     */
    fun loadPagedReviews(
        productId: String,
        page: Int = 0,
        pageSize: Int = Const.PAGE_SIZE,
        orderBy: CommentSwitch.Type?
    ): Single<Page<Comment>>

    /**
     * 获取单个点评
     */
    fun getReviewById(reviewId: String): Single<Comment>

    /**
     * 分页的评论列表
     * 对于子评论 children，目前这样处理：
     * 如果有子评论的话，则 children 包含一个真实的子评论和一个加载更多 Comment
     */
    fun loadPagedComment (
        productId: String,
        reviewId: String,
        page: Int = 0,
        pageSize: Int = Const.PAGE_SIZE
    ): Single<Page<Comment>>

    /**
     * 分页的二级评论列表
     */
    fun loadPagedChildComment (
        productId: String,
        reviewId: String,
        parentId: String,
        offset: Int
    ): Single<PageByOffset<Comment>>

    /**
     * 产品的标签列表
     */
    fun loadAllTags(productId: String): Single<List<Comment>>
}