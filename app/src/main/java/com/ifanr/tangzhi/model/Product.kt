package com.ifanr.tangzhi.model

import com.google.gson.JsonObject
import com.ifanr.tangzhi.Const
import com.ifanr.tangzhi.ext.*
import com.minapp.android.sdk.database.Record

/**
 * 对应后端数据表：product
 */
data class Product (

    override val id: String = "",

    /**
     * 名称，eg："Home Pod"
     */
    val name: String = "",

    /**
     * 类型，eg："hardware"
     * @see TYPE_FEATURE
     * @see TYPE_HARDWARE
     */
    val type: String = "",

    /**
     * 分类，eg：["苹果", "音响"]
     */
    val category: List<String> = emptyList(),

    /**
     * 标签（有延迟，5 min 更新一次），eg：
     * [
     *   {
     *     // 标签的信息
     *   }
     * ]
     */
    val tag: List<JsonObject> = emptyList(),

    /**
     * 产品主图，eg："https://ifanr.com/logo.jpg"
     */
    val coverImage: String = "",

    /**
     * 产品图标（指产品的无背景抠图），eg："https://ifanr.com/logo.jpg"
     */
    val icon: String = "",

    /**
     * 产品图片列表，eg：["https://ifanr.com/logo.jpg", "https://ifanr.com/logo.jpg"]
     */
    val image: List<String> = emptyList(),

    /**
     * 编辑从用户评论中选取的照片，eg：
     * [
     *   {
     *     "index": 1,
     *     "comment": {
     *       // 点评的信息
     *     }
     *   }
     * ]
     */
    val recommendedImage: List<JsonObject> = emptyList(),

    /**
     * 一句话简介，eg："你的手机能拍月亮吗？"
     */
    val brief: String = "",

    /**
     * 产品描述，eg："这里是产品的描述"
     */
    val description: String = "",

    /**
     * 状态
     * @see STATUS_APPROVED
     * @see STATUS_PENDING
     * @see STATUS_DRAFT
     * @see STATUS_DELETED
     */
    val status: String = "",

    /**
     * 关注人数，eg：129099
     */
    val followerCount: Long = 0,


    /**
     * 关注人数偏置值，eg：1000
     */
    val followerCountOffset: Long = 0,


    /**
     * 解锁条件（关注人数达到解锁条件则安排产品上线），eg：1000
     */
    val condition: Long = 0,


    /**
     * 关联文章的数量，eg：1
     */
    val postCount: Long = 0,


    /**
     * 点评数量
     */
    val reviewCount: Long = 0,


    /**
     * 提问数
     */
    val questionCount: Long = 0,


    /**
     * 回答数
     */
    val answerCount: Long = 0,


    /**
     * 固定位置，例如： 1 代表在产品列表中，该产品的位置恒定固定在第 1 位
     */
    val displayPosition: Long = 0,


    /**
     * 评分
     */
    val rating: Float = 0f,


    /**
     * 机构评分
     */
    val orgRating: List<ThirdPartyRating> = emptyList(),


    /**
     * 用户评分
     */
    val userRating: Float = 0f,


    /**
     * 产品主题颜色，用作前端显示时，界面背景色，eg："#456744"
     */
    val theme: String = "",


    /**
     * 最近参与者的头像，最大长度为 5，eg：
     * ["https://ifanr.com/logo.jpg", "https://ifanr.com/logo.jpg"]
     */
    val participant: List<String> = emptyList(),


    /**
     * 历史参与人数
     */
    val participantCount: Long = 0L,


    /**
     * 最近参与者的 id，eg：[32268035, 32268036]
     */
    val participantId: List<Long> = emptyList(),


    /**
     * 是否已发送产品发布通知（注意：默认是已发送的）
     */
    val notified: Boolean = false,


    /**
     * 是否需要重新计算产品的分数
     */
    val ratingNeedUpdate: Boolean = false,


    /**
     * 产品发布时间，可不填，表示发布时间未知，eg：1566715331
     */
    val releasedAt: Long = 0,


    /**
     * 产品发布是否已确定：
     * true : 发布时间已确定，前端展示具体的发布日期
     * false：发布时间未确定，前端展示上旬、中旬、下旬等形式
     */
    val releasedConfirmed: Boolean = false,


    /**
     * 产品优先级，数字越大，优先级越高，目前用于新品的排序
     */
    val priority: Long = 0,


    /**
     * 产品的基础参数 ID
     */
    val paramId: String = "",


    /**
     * 表示产品的参数是否被抓取过
     */
    val paramCrawled: Boolean = false,


    /**
     * 基础参数是否可见
     */
    val paramVisible: Boolean = false,


    /**
     * 亮点参数
     */
    val highlightParam: List<HighlightParam> = emptyList(),


    /**
     * 亮点参数是否可见
     */
    val highlightParamVisible: Boolean = false,


    /**
     * 讨论数量
     */
    val discussionCount: Long = 0L,


    /**
     * 第三方评分，第三方评分不计入模范指数，只做前端展示
     */
    val thirdPartyRating: List<ThirdPartyRating> = emptyList(),

    /**
     * 缓存主站的文章摘要信息，用于小程序首页精选产品列表关联文章卡片的显示
     */
    val cachedPost: List<CachedPost> = emptyList(),

    /**
     * 同类产品，用于产品详情页推荐产品的相关产品，eg：
     * ["5c0e04b10307b352c4aee0c1"]
     */
    val similarProduct: List<String> = emptyList()

) : BaseModel {

    constructor(record: Record): this (
        id = record.getSafeId(),
        name = record.getSafeString(COL_NAME),
        type = record.getSafeString(COL_TYPE),
        category = record.getSafeStringArray(COL_CATEGORY),
        tag = record.getSafeArray(COL_TAG),
        coverImage = record.getSafeString(COL_COVER_IMAGE),
        icon = record.getSafeString(COL_ICON),
        image = record.getSafeStringArray(COL_IMAGE),
        recommendedImage = record.getSafeArray(COL_RECOMMENDED_IMAGE),
        brief = record.getSafeString(COL_BRIEF),
        description = record.getSafeString(COL_DESCRIPTION),
        status = record.getSafeString(COL_STATUS),
        followerCount = record.getSafeLong(COL_FOLLOWER_COUNT),
        followerCountOffset = record.getSafeLong(COL_FOLLOWER_COUNT_OFFSET),
        condition = record.getSafeLong(COL_CONDITION),
        postCount = record.getSafeLong(COL_POST_COUNT),
        reviewCount = record.getSafeLong(COL_REVIEW_COUNT),
        questionCount = record.getSafeLong(COL_QUESTION_COUNT),
        answerCount = record.getSafeLong(COL_ANSWER_COUNT),
        displayPosition = record.getSafeLong(COL_DISPLAY_POSITION),
        rating = record.getSafeFloat(COL_RATING),
        orgRating = record.getSafeArrayByConstruct(COL_ORG_RATING, ThirdPartyRating::class.java),
        userRating = record.getSafeFloat(COL_USER_RATING),
        theme = record.getSafeString(COL_THEME),
        participant = record.getSafeStringArray(COL_PARTICIPANT),
        participantCount = record.getSafeLong(COL_PARTICIPANT_COUNT),
        participantId = record.getSafeArray(COL_PARTICIPANT_ID, Long::class.java),
        notified = record.getSafeBoolean(COL_NOTIFIED),
        ratingNeedUpdate = record.getSafeBoolean(COL_RATING_NEED_UPDATE),
        releasedAt = record.getSafeLong(COL_RELEASED_AT),
        releasedConfirmed = record.getSafeBoolean(COL_RELEASED_COONFIRMED),
        priority = record.getSafeLong(COL_PRIORITY),
        paramId = record.getSafeString(COL_PARAM_ID),
        paramCrawled = record.getSafeBoolean(COL_PARAM_CRAWLED),
        paramVisible = record.getSafeBoolean(COL_PARAM_VISIBLE),
        highlightParam = record.getSafeArrayByConstruct(COL_HIGHLIGHT_PARAM, HighlightParam::class.java),
        highlightParamVisible = record.getSafeBoolean(COL_HIGHLIGHT_PARAM_VISIBLE),
        discussionCount = record.getSafeLong(COL_DISCUSSION_COUNT),
        thirdPartyRating = record.getSafeArrayByConstruct(COL_THIRD_PARTY_RATING, ThirdPartyRating::class.java),
        cachedPost = record.getSafeArrayByConstruct(COL_CACHED_POST, CachedPost::class.java),
        similarProduct = record.getSafeStringArray(COL_SIMILAR_PRODUCT)
    )

    companion object {

        const val TYPE_HARDWARE = "hardware"    // 硬件
        const val TYPE_FEATURE = "feature"      // 专题

        const val STATUS_APPROVED = "approved"  // 已发布
        const val STATUS_PENDING = "pending"    // 未发布
        const val STATUS_DRAFT = "draft"        // 草稿
        const val STATUS_DELETED = "deleted"    // 已删除

        const val COL_NAME = "name"
        const val COL_TYPE = "type"
        const val COL_CATEGORY = "category"
        const val COL_TAG = "tag"
        const val COL_COVER_IMAGE = "cover_image"
        const val COL_ICON = "icon"
        const val COL_IMAGE = "image"
        const val COL_RECOMMENDED_IMAGE = "recommended_image"
        const val COL_BRIEF = "brief"
        const val COL_DESCRIPTION = "description"
        const val COL_STATUS = "status"
        const val COL_FOLLOWER_COUNT = "follower_count"
        const val COL_FOLLOWER_COUNT_OFFSET = "follower_count_offset"
        const val COL_CONDITION = "condition"
        const val COL_POST_COUNT = "post_count"
        const val COL_REVIEW_COUNT = "review_count"
        const val COL_QUESTION_COUNT = "question_count"
        const val COL_ANSWER_COUNT = "answer_count"
        const val COL_DISPLAY_POSITION = "display_position"
        const val COL_RATING = "rating"
        const val COL_ORG_RATING = "org_rating"
        const val COL_USER_RATING = "user_rating"
        const val COL_THEME = "theme"
        const val COL_PARTICIPANT = "participant"
        const val COL_PARTICIPANT_COUNT = "participant_count"
        const val COL_PARTICIPANT_ID = "participant_id"
        const val COL_NOTIFIED = "notified"
        const val COL_RATING_NEED_UPDATE = "rating_need_update"
        const val COL_RELEASED_AT = "released_at"
        const val COL_RELEASED_COONFIRMED = "released_confirmed"
        const val COL_PRIORITY = "priority"
        const val COL_CACHED_POST = "cached_post"
        const val COL_PARAM_ID = "param_id"
        const val COL_PARAM_CRAWLED = "param_crawled"
        const val COL_PARAM_VISIBLE = "param_visible"
        const val COL_HIGHLIGHT_PARAM = "highlight_param"
        const val COL_HIGHLIGHT_PARAM_VISIBLE = "highlight_param_visible"
        const val COL_DISCUSSION_COUNT = "discussion_count"
        const val COL_THIRD_PARTY_RATING = "third_party_rating"
        const val COL_GROUP = "group"
        const val COL_SIMILAR_PRODUCT = "similar_product"
        const val COL_WECHAT_SEARCH_UPDATE_NEEDED = "wechat_search_update_needed"
        const val COL_WECHAT_SEARCH_CHECKSUM = "wechat_search_checksum"
        const val COL_TOPIC = "topic"
        const val COL_RELATED_PRODUCT = "related_product"
        const val COL_SHARE_COVER = "share_cover"
    }

    /**
     * 转化成 int 的主题颜色
     */
    val themeColor: Int
        get() = runCatching { theme.toColorInt() }.getOrDefault(Const.DEFAULT_PRODUCT_THEME)


    data class ThirdPartyRating (
        /**
         * 机构名称
         */
        val name: String = "",

        /**
         * 机构打分
         */
        val rating: Float = 0f
    ) {

        companion object {
            const val COL_NAME = "name"
            const val COL_RATING = "rating"
        }

        constructor(json: JsonObject): this (
            name = json.getSafeString(COL_NAME),
            rating = json.getSafeFloat(COL_RATING)
        )
    }

    data class HighlightParam (
        /**
         * 参数名称
         */
        val key: String = "",

        /**
         * 参数值描述
         */
        val value: String = ""
    ) {

        companion object {
            const val COL_KEY = "key"
            const val COL_VALUE = "value"
        }

        constructor(json: JsonObject): this (
            key = json.getSafeString(COL_KEY),
            value = json.getSafeString(COL_VALUE)
        )
    }

    data class CachedPost (
        /**
         * 主站 post id
         */
        val postId: String = "",

        /**
         * 封面图
         */
        val postCoverImage: String = "",

        val postTitle: String = "",

        val createdByName: String = "",

        val createdByAvatar: String = "",

        /**
         * product_post id
         */
        val id: String = "",

        val thirdParty: Boolean = false,

        val tag: List<String> = emptyList()
    ) {

        companion object {
            const val COL_POST_ID = "post_id"
            const val COL_POST_COVER_IMAGE = "post_cover_image"
            const val COL_POST_TITLE = "post_title"
            const val COL_ID = "id"
            const val COL_THIRD_PARTY = "third_party"
            const val COL_TAG = "tag"
        }

        constructor(json: JsonObject): this (
            postId = json.getSafeString(COL_POST_ID),
            postCoverImage = json.getSafeString(COL_POST_COVER_IMAGE),
            postTitle = json.getSafeString(COL_POST_TITLE),
            createdByName = kotlin.runCatching {
                json["created_by"].asJsonObject.getSafeString("name")
            }.getOrDefault(""),
            createdByAvatar = kotlin.runCatching {
                json["created_by"].asJsonObject.getSafeString("avatar")
            }.getOrDefault(""),
            id = json.getSafeString(COL_ID),
            thirdParty = json.getSafeBoolean(COL_THIRD_PARTY),
            tag = json.getSafeStringArray(COL_TAG)

        )

    }
}