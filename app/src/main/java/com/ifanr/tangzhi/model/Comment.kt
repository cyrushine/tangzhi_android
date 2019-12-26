package com.ifanr.tangzhi.model

import android.util.Log
import androidx.core.graphics.toColorInt
import com.ifanr.tangzhi.Const
import com.ifanr.tangzhi.ext.*
import com.minapp.android.sdk.database.Record

class Comment {

    var id: String = ""
    var content: String = ""
    var upvote: Int = 0
    var downvote: Int = 0
    var replyCount: Int = 0
    var recommended: Boolean = false
    var images: List<String> = emptyList()
    var theme: Int = Const.DEFAULT_PRODUCT_THEME
    var description: String = ""

    constructor(record: Record) {
        id = record.getSafeId()
        content = record.getSafeString(COL_CONTENT)
        upvote = record.getSafeInt(COL_UPVOTE)
        downvote = record.getSafeInt(COL_DOWNVOTE)
        replyCount = record.getSafeInt(COL_REPLY_COUNT)
        recommended = record.getSafeBoolean(COL_RECOMMENDED)
        images = record.getSafeStringArray(COL_IMAGE)
        theme = record.getSafeString(COL_THEME).toSafeColorInt()
        description = record.getSafeString(COL_DESCRIPTION)

    }

    companion object {

        private const val TAG = "Comment"

        const val TYPE_REVIEW         = "review"           // 点评
        const val TYPE_QUESTION       = "question"         // 提问
        const val TYPE_ANSWER         = "answer"           // 问答
        const val TYPE_COMMENT        = "comment"          // 评论
        const val TYPE_TAG            = "tag"              // 标签
        const val TYPE_TESTING_REVIEW = "testing_review"   // 众测点评
        const val TYPE_DISCUSSION     = "discussion"       // 讨论

        /**
         * 类型
         * @see TYPE_REVIEW
         * @see TYPE_QUESTION
         * @see TYPE_ANSWER
         * @see TYPE_COMMENT
         * @see TYPE_TAG
         * @see TYPE_TESTING_REVIEW
         * @see TYPE_DISCUSSION
         */
        const val COL_TYPE                 = "type"

        /**
         * 关联产品
         * pointer
         */
        const val COL_PRODUCT              = "product"

        /**
         * 根评论id：
         * type = answer / comment 类型的评论，root_id 指向其所属的点评/提问 id
         * type = review / question / tag 类型的评论， root_id 为 undefined
         */
        const val COL_ROOT_ID              = "root_id"

        /**
         * 父评论id（一级评论没有此属性，二级评论则指向某个一级评论的 id ）
         */
        const val COL_PARENT_ID            = "parent_id"

        /**
         * 指向评论回复的评论（只有 type = answer / comment 才有此属性）
         */
        const val COL_REPLY_ID             = "reply_id"

        /**
         * 评论内容（当为 review 类型，不是必选项）
         */
        const val COL_CONTENT              = "content"

        const val COL_UPVOTE               = "upvote"
        const val COL_DOWNVOTE             = "downvote"
        const val COL_REPLY_COUNT          = "reply_count"

        /**
         * 回复对象，指向 _userprofile （只有 type = answer / comment 才有此属性）
         * pointer
         */
        const val COL_REPLY_TO             = "reply_to"
        const val COL_STATUS               = "status"
        const val COL_REPORTED_BY          = "reported_by"
        const val COL_RECOMMENDED          = "recommended"
        const val COL_NOTIFIED             = "notified"
        const val COL_RECOMMENDED_NOTIFIED = "recommended_notified"
        const val COL_UPVOTE_NOTIFIED      = "upvote_notified"
        const val COL_UPDATED_BY           = "updated_by"
        const val COL_POINT_GIVEN          = "point_given"
        const val COL_TESTING              = "testing"
        const val COL_TOPIC                = "topic"

        /**
         * 设为置顶
         * boolean
         */
        const val COL_TOPPING              = "topping"
        const val COL_TITLE                = "title"
        const val COL_CREATED_BY_USER      = "created_by_user"

        /**
         * 评论图片，最多限制 9 张
         * string array
         */
        const val COL_IMAGE                = "image"
        const val COL_RATING               = "rating"
        const val COL_IS_POSITIVE          = "is_positive"
        const val COL_PARTICIPANT          = "participant"
        const val COL_THEME                = "theme"
        const val COL_DESCRIPTION          = "description"
    }
}