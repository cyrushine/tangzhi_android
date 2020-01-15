package com.ifanr.tangzhi.repository.baas

import androidx.annotation.FloatRange
import androidx.paging.PagedList
import com.ifanr.tangzhi.Const
import com.ifanr.tangzhi.model.*
import com.ifanr.tangzhi.ui.widgets.CommentSwitch
import com.minapp.android.sdk.storage.CloudFile
import io.reactivex.Completable
import io.reactivex.Single

interface BaasRepository {

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
     * @param images 图片会上传到服务器，然后使用服务器地址
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