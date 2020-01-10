package com.ifanr.tangzhi.repository.baas

import androidx.paging.PagedList
import com.ifanr.tangzhi.Const
import com.ifanr.tangzhi.model.*
import com.ifanr.tangzhi.ui.widgets.CommentSwitch
import com.minapp.android.sdk.storage.CloudFile
import io.reactivex.Completable
import io.reactivex.Single

interface BaasRepository {

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