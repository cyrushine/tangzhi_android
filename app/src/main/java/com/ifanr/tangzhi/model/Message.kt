package com.ifanr.tangzhi.model

import com.google.gson.JsonObject
import com.ifanr.tangzhi.ext.getSafeJson
import com.ifanr.tangzhi.ext.getSafeLong
import com.ifanr.tangzhi.ext.getSafeString
import com.ifanr.tangzhi.ext.getSafeStringArray
import com.ifanr.tangzhi.repository.baas.Tables
import com.minapp.android.sdk.database.Record
import com.minapp.android.sdk.database.Table
import com.minapp.android.sdk.user.User

class Message {

    constructor()

    constructor(record: Record) {
        type = record.getString(COL_TYPE)
        action = record.getString(COL_ACTION)
        detail = Detail(record.getSafeJson(COL_DETAIL))
        read = record.getBoolean(COL_READ)
        createdAt = record.getSafeLong(Record.CREATED_AT)
        sender = UserProfile(User(Tables.userprofile, record.getSafeJson(COL_SENDER)))
        id = record.getSafeString(Record.ID)
    }


    companion object {
        const val COL_TYPE   = "type"
        const val COL_ACTION = "action"
        const val COL_DETAIL = "detail"
        const val COL_SENDER = "sender"
        const val COL_READ   = "read"

        const val TYPE_PRODUCT = "product"  // 产品消息
        const val TYPE_SYSTEM = "system"    // 系统消息
        const val TYPE_TESTING = "testing"  // 众测消息

        const val ACTION_REVIEW          = "review"           // type == product，点评/回复点评/回复点评下的评论
        const val ACTION_QUESTION        = "question"         // type == product，回答问题
        const val ACTION_TESTING_REVIEW  = "testing_review"   // 回复众测点评 / 或回复众测点评下的评论
        const val ACTION_DISCUSSION      = "discussion"       // 回复讨论/回复讨论下的评论
        const val ACTION_RECOMMEND       = "recommend"        // type == system，评论精选通知
        const val ACTION_UPVOTES         = "upvotes"          // type == system，评论多人点赞通知
        const val ACTION_ANSWER_INVATION = "answer_invation"  // type == system，问题回答邀请
    }

    /**
     * @see TYPE_PRODUCT
     * @see TYPE_SYSTEM
     * @see TYPE_TESTING
     */
    var type: String? = null

    /**
     * @see ACTION_REVIEW
     * @see ACTION_QUESTION
     * @see ACTION_TESTING_REVIEW
     * @see ACTION_DISCUSSION
     * @see ACTION_RECOMMEND
     * @see ACTION_UPVOTES
     * @see ACTION_ANSWER_INVATION
     */
    var action: String? = null

    /**
     * type 为 product 时：
     * {
     *      "product": {
     *          // 产品信息
     *      },
     *      "comment": {
     *          // 评论信息
     *      },
     *      "parent_comment": {
     *          // 父评论信息
     *      },
     *      "root_comment": {
     *          // 根评论信息
     *      },
     *      "reply_comment": {
     *          // 被回复的评论的信息
     *      }
     * }
     *
     * type 为 testing_review 时：
     * {
     *      "product": {
     *          // 产品信息
     *      },
     *      "comment": {
     *          // 评论信息
     *      },
     *      "parent_comment": {
     *          // 父评论信息
     *      },
     *      "root_comment": {
     *          // 根评论信息
     *      },
     *      "reply_comment": {
     *          // 被回复的评论的信息
     *      },
     *      "testing": {
     *          // 众测信息
     *      }
     * }
     *
     * type 为 system - upvote 时
     * {
     *      "product": {
     *          // 产品信息
     *      },
     *      "comment": {
     *          // 评论信息
     *      },
     *      "parent_comment": {
     *          // 父评论信息(可选)
     *      },
     *      "root_comment": {
     *          // 根评论信息（可选）
     *      },
     *
     *      // 参与者名称
     *      participant: ["张三","李四"]
     * }
     *
     * type 为 system - recommemd 时
     * {
     *      "product": {
     *          // 产品信息
     *      },
     *      "comment": {
     *          // 评论信息
     *      }
     * }
     *
     * type 为 system - answer_invation 时：
     * {
     *      "comment": {
     *          // 评论信息
     *      }
     * }
     */
    var detail: Detail? = null

    /**
     * 是否已阅读
     */
    var read: Boolean? = null

    var createdAt: Long = 0

    // 关联的发送者
    var sender: UserProfile? = null

    var id: String? = null


    class Detail {

        constructor()

        constructor(json: JsonObject) {
            try {
                product = Product(Record(Tables.product, json.getAsJsonObject(COL_PRODUCT)))
            } catch (e: Exception) {}
            try {
                comment = Comment(Record(Tables.comment, json.getAsJsonObject(COL_COMMENT)))
            } catch (e: Exception) {}
            try {
                parentComment = Comment(Record(Tables.comment, json.getAsJsonObject(COL_PARENT_COMMENT)))
            } catch (e: Exception) {}
            try {
                rootComment = Comment(Record(Tables.comment, json.getAsJsonObject(COL_ROOT_COMMENT)))
            } catch (e: Exception) {}
            try {
                replyComment = Comment(Record(Tables.comment, json.getAsJsonObject(COL_REPLY_COMMENT)))
            } catch (e: Exception) {}
            participant = json.getSafeStringArray(COL_PARTICIPANT)
        }


        companion object {
            const val COL_PRODUCT        = "product"
            const val COL_COMMENT        = "comment"
            const val COL_PARENT_COMMENT = "parent_comment"
            const val COL_ROOT_COMMENT   = "root_comment"
            const val COL_REPLY_COMMENT  = "reply_comment"
            const val COL_TESTING        = "testing"
            const val COL_PARTICIPANT    = "participant"
        }

        // 产品信息
        var product: Product? = null

        // 评论信息
        var comment: Comment? = null

        // 父评论信息
        var parentComment: Comment? = null

        // 根评论信息
        var rootComment: Comment? = null

        // 被回复的评论的信息
        var replyComment: Comment? = null

        // 参与者名称
        var participant: List<String>? = null

    }
}