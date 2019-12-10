package com.ifanr.android.tangzhi.model

import androidx.annotation.ColorInt
import com.google.gson.JsonObject
import com.ifanr.android.tangzhi.Const
import com.ifanr.android.tangzhi.ext.*
import com.minapp.android.sdk.database.Record
import java.math.RoundingMode
import java.text.NumberFormat

/**
 * 对应后端数据表：product
 */
class Product: Record {

    constructor(base: Record): super(base._getTable(), base._getJson())

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
     * 名称，eg："Home Pod"
     */
    var name: String?
        get() = getString(COL_NAME)
        set(value) { put(COL_NAME, value) }

    /**
     * 类型，eg："hardware"
     * @see TYPE_FEATURE
     * @see TYPE_HARDWARE
     */
    var type: String?
        get() = getString(COL_TYPE)
        set(value) { put(COL_TYPE, value) }

    /**
     * 分类，eg：["苹果", "音响"]
     */
    var category: List<String>?
        get() = getArray(COL_CATEGORY, String::class.java)
        set(value) { put(COL_CATEGORY, value) }

    /**
     * 标签（有延迟，5 min 更新一次），eg：
     * [
     *   {
     *     // 标签的信息
     *   }
     * ]
     */
    var tag: List<JsonObject>?
        get() = getArray(COL_TAG, JsonObject::class.java)
        set(value) { put(COL_TAG, value) }

    /**
     * 产品主图，eg："https://ifanr.com/logo.jpg"
     */
    var coverImage: String?
        get() = getString(COL_COVER_IMAGE)
        set(value) { put(COL_COVER_IMAGE, value) }

    /**
     * 产品图标（指产品的无背景抠图），eg："https://ifanr.com/logo.jpg"
     */
    var icon: String?
        get() = getString(COL_ICON)
        set(value) { put(COL_ICON, value) }

    /**
     * 产品图片列表，eg：["https://ifanr.com/logo.jpg", "https://ifanr.com/logo.jpg"]
     */
    var image: List<String>?
        get() = getArray(COL_IMAGE, String::class.java)
        set(value) { put(COL_IMAGE, value) }

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
    var recommendedImage: List<JsonObject>?
        get() = getArray(COL_RECOMMENDED_IMAGE, JsonObject::class.java)
        set(value) { put(COL_RECOMMENDED_IMAGE, value) }

    /**
     * 一句话简介，eg："你的手机能拍月亮吗？"
     */
    var brief: String?
        get() = getString(COL_BRIEF)
        set(value) { put(COL_BRIEF, value) }

    /**
     * 产品描述，eg："这里是产品的描述"
     */
    var description: String?
        get() = getString(COL_DESCRIPTION)
        set(value) { put(COL_DESCRIPTION, value) }

    /**
     * 状态
     * @see STATUS_APPROVED
     * @see STATUS_PENDING
     * @see STATUS_DRAFT
     * @see STATUS_DELETED
     */
    var status: String?
        get() = getString(COL_STATUS)
        set(value) { put(COL_STATUS, value) }

    /**
     * 关注人数，eg：129099
     */
    var followerCount: Long?
        get() = getLong(COL_FOLLOWER_COUNT)
        set(value) { put(COL_FOLLOWER_COUNT, value) }


    /**
     * 关注人数偏置值，eg：1000
     */
    var followerCountOffset: Long?
        get() = getLong(COL_FOLLOWER_COUNT_OFFSET)
        set(value) { put(COL_FOLLOWER_COUNT_OFFSET, value) }


    /**
     * 解锁条件（关注人数达到解锁条件则安排产品上线），eg：1000
     */
    var condition: Long?
        get() = getLong(COL_CONDITION)
        set(value) { put(COL_CONDITION, value) }


    /**
     * 关联文章的数量，eg：1
     */
    var postCount: Long
        get() = getLong(COL_POST_COUNT) ?: 0L
        set(value) { put(COL_POST_COUNT, value) }


    /**
     * 点评数量
     */
    var reviewCount: Long?
        get() = getLong(COL_REVIEW_COUNT)
        set(value) { put(COL_REVIEW_COUNT, value) }


    /**
     * 提问数
     */
    var questionCount: Long?
        get() = getLong(COL_QUESTION_COUNT)
        set(value) { put(COL_QUESTION_COUNT, value) }


    /**
     * 回答数
     */
    var answerCount: Long?
        get() = getLong(COL_ANSWER_COUNT)
        set(value) { put(COL_ANSWER_COUNT, value) }


    /**
     * 固定位置，例如： 1 代表在产品列表中，该产品的位置恒定固定在第 1 位
     */
    var displayPosition: Long?
        get() = getLong(COL_DISPLAY_POSITION)
        set(value) { put(COL_DISPLAY_POSITION, value) }


    /**
     * 评分
     */
    var rating: Float
        get() = getFloat(COL_RATING) ?: 0f
        set(value) { put(COL_RATING, value) }


    /**
     * 机构评分
     */
    val orgRating: List<ThirdPartyRating>?
        get() = getArray(COL_ORG_RATING, JsonObject::class.java)?.map { ThirdPartyRating(it) }


    /**
     * 用户评分
     */
    var userRating: Long?
        get() = getLong(COL_USER_RATING)
        set(value) { put(COL_USER_RATING, value) }


    /**
     * 产品主题颜色，用作前端显示时，界面背景色，eg："#456744"
     */
    var theme: String?
        get() = getString(COL_THEME)
        set(value) { put(COL_THEME, value) }

    /**
     * 转化成 int 的主题颜色
     */
    val themeColor: Int
        get() = theme?.toColorInt() ?: Const.DEFAULT_PRODUCT_THEME


    /**
     * 最近参与者的头像，最大长度为 5，eg：
     * ["https://ifanr.com/logo.jpg", "https://ifanr.com/logo.jpg"]
     */
    var participant: List<String>?
        get() = getArray(COL_PARTICIPANT, String::class.java)
        set(value) { put(COL_PARTICIPANT, value) }


    /**
     * 历史参与人数
     */
    var participantCount: Long?
        get() = getLong(COL_PARTICIPANT_COUNT)
        set(value) { put(COL_PARTICIPANT_COUNT, value) }


    /**
     * 最近参与者的 id，eg：[32268035, 32268036]
     */
    var participantId: List<Long>?
        get() = getArray(COL_PARTICIPANT_ID, Long::class.java)
        set(value) { put(COL_PARTICIPANT_ID, value) }


    /**
     * 是否已发送产品发布通知（注意：默认是已发送的）
     */
    var notified: Boolean?
        get() = getBoolean(COL_NOTIFIED)
        set(value) { put(COL_NOTIFIED, value) }


    /**
     * 是否需要重新计算产品的分数
     */
    var ratingNeedUpdate: Boolean?
        get() = getBoolean(COL_RATING_NEED_UPDATE)
        set(value) { put(COL_RATING_NEED_UPDATE, value) }


    /**
     * 产品发布时间，可不填，表示发布时间未知，eg：1566715331
     */
    var releasedAt: Long?
        get() = getLong(COL_RELEASED_AT)
        set(value) { put(COL_RELEASED_AT, value) }


    /**
     * 产品发布是否已确定：
     * true : 发布时间已确定，前端展示具体的发布日期
     * false：发布时间未确定，前端展示上旬、中旬、下旬等形式
     */
    var releasedConfirmed: Boolean?
        get() = getBoolean(COL_RELEASED_COONFIRMED)
        set(value) { put(COL_RELEASED_COONFIRMED, value) }


    /**
     * 产品优先级，数字越大，优先级越高，目前用于新品的排序
     */
    var priority: Long?
        get() = getLong(COL_PRIORITY)
        set(value) { put(COL_PRIORITY, value) }


    /**
     * 产品的基础参数 ID
     */
    var paramId: Long?
        get() = getLong(COL_PARAM_ID)
        set(value) { put(COL_PARAM_ID, value) }


    /**
     * 表示产品的参数是否被抓取过
     */
    var paramCrawled: Boolean?
        get() = getBoolean(COL_PARAM_CRAWLED)
        set(value) { put(COL_PARAM_CRAWLED, value) }


    /**
     * 基础参数是否可见
     */
    var paramVisible: Boolean?
        get() = getBoolean(COL_PARAM_VISIBLE)
        set(value) { put(COL_PARAM_VISIBLE, value) }


    /**
     * 亮点参数
     */
    var highlightParam: List<HighlightParam>?
        get() = getArray(COL_HIGHLIGHT_PARAM, JsonObject::class.java)?.map { HighlightParam(it) }
        set(value) { put(COL_HIGHLIGHT_PARAM, value) }


    /**
     * 亮点参数是否可见
     */
    var highlightParamVisible: Boolean?
        get() = getBoolean(COL_HIGHLIGHT_PARAM_VISIBLE)
        set(value) { put(COL_HIGHLIGHT_PARAM_VISIBLE, value) }


    /**
     * 讨论数量
     */
    var discussionCount: Long?
        get() = getLong(COL_DISCUSSION_COUNT)
        set(value) { put(COL_DISCUSSION_COUNT, value) }


    /**
     * 第三方评分，第三方评分不计入模范指数，只做前端展示
     */
    val thirdPartyRating: List<ThirdPartyRating>?
        get() = getArray(COL_THIRD_PARTY_RATING, JsonObject::class.java)?.map { ThirdPartyRating(it) }

    /**
     * 缓存主站的文章摘要信息，用于小程序首页精选产品列表关联文章卡片的显示
     */
    val cachedPost: List<CachedPost>?
        get() = getArray(COL_CACHED_POST, JsonObject::class.java)?.map { CachedPost(it) }

    /**
     * 同类产品，用于产品详情页推荐产品的相关产品，eg：
     * ["5c0e04b10307b352c4aee0c1"]
     */
    val similarProduct: List<String>
        get() = getArray(COL_SIMILAR_PRODUCT, String::class.java) ?: emptyList()


    class ThirdPartyRating (
        private val data: JsonObject
    ) {

        /**
         * 机构名称
         */
        val name: String?
            get() = data.getAsString("name")

        /**
         * 机构打分
         */
        val rating: Float?
            get() = data.getAsFloat("rating")
    }

    class HighlightParam (
        private val data: JsonObject
    ) {

        /**
         * 参数名称
         */
        val key: String?
            get() = data.getAsString("key")

        /**
         * 参数值描述
         */
        val value: String?
            get() = data.getAsString("value")
    }

    class CachedPost (
        private val data: JsonObject
    ) {

        /**
         * 主站 post id
         */
        val postId: String?
            get() = data.getAsString("post_id")

        /**
         * 封面图
         */
        val postCoverImage: String?
            get() = data.getAsString("post_cover_image")

        val postTitle: String?
            get() = data.getAsString("post_title")

        val createdByName: String?
            get() = runCatching { data["created_by"].asJsonObject.getAsString("name") }.getOrNull()

        val createdByAvatar: String?
            get() = runCatching { data["created_by"].asJsonObject.getAsString("avatar") }.getOrNull()

        /**
         * product_post id
         */
        val id: String?
            get() = data.getAsString("id")

        val thirdParty: Boolean?
            get() = data.getAsBoolean("third_party")

        val tag: List<String>?
            get() = data.getAsStringArray("tag")

    }
}