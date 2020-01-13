package com.ifanr.tangzhi.model

import com.google.gson.JsonObject
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getSafeInt
import com.ifanr.tangzhi.ext.getSafeJson
import com.ifanr.tangzhi.ext.getSafeLong
import com.ifanr.tangzhi.ext.getSafeString
import com.ifanr.tangzhi.util.uuid
import com.minapp.android.sdk.database.Record

/**
 * 积分记录表
 */
class PointLog {

    constructor(record: Record) {
        type = record.getSafeString(COL_TYPE)
        action = record.getSafeString(COL_ACTION)
        subjectId = record.getSafeString(COL_SUBJECT_ID)
        point = record.getSafeInt(COL_POINT)
        detail = record.getSafeJson(COL_DETAIL)
        createdAtSecond = record.getSafeLong(Record.CREATED_AT)
        id = record.id ?: uuid()
    }

    constructor()

    companion object {

        const val TYPE_CONTENT = "content"        // 发表内容
        const val TYPE_REPLY = "reply"            // 参与回复
        const val TYPE_TESTING = "testing"        // 参与众测
        const val TYPE_ITEM_LIST = "item_list"    // 清单奖励
        const val TYPE_SYSTEM = "system"          // 系统奖励
        const val TYPE_REVOKE = "revoke"          // 积分回收

        const val ACTION_CONTENT_REVIEW                      = "review"
        const val ACTION_CONTENT_DISCUSSION                  = "discussion"
        const val ACTION_CONTENT_QUESTION                    = "question"
        const val ACTION_CONTENT_TESTING_REVIEW              = "testing_review"
        const val ACTION_REPLY_REVIEW                        = "review"
        const val ACTION_REPLY_DISCUSSION                    = "discussion"
        const val ACTION_REPLY_QUESTION                      = "question"
        const val ACTION_REPLY_TESTING_REVIEW                = "testing_review"
        const val ACTION_REPLY_ITEM_LIST                     = "item_list"
        const val ACTION_TESTING_RESERVED                    = "reserved"
        const val ACTION_TESTING_ASSISTED                    = "assisted"
        const val ACTION_TESTING_APPLYING                    = "applying"
        const val ACTION_TESTING_APPLIED                     = "applied"
        const val ACTION_TESTING_SURVEY_FILLED               = "survey_filled"
        const val ACTION_TESTING_CONFIRMED                   = "confirmed"
        const val ACTION_ITEM_LIST_CREATE                    = "create"
        const val ACTION_SYSTEM_RECOMMENDED_REVIEW           = "recommended_review"
        const val ACTION_SYSTEM_RECOMMENDED_DISCUSSION       = "recommended_discussion"
        const val ACTION_SYSTEM_RECOMMENDED_QUESTION         = "recommended_question"
        const val ACTION_SYSTEM_RECOMMENDED_ANSWER           = "recommended_answer"
        const val ACTION_SYSTEM_TOPPING_QUESTION             = "topping_question"
        const val ACTION_SYSTEM_TOPPING_DISCUSSION           = "topping_discussion"
        const val ACTION_SYSTEM_REWARD                       = "reward"
        const val ACTION_SYSTEM_RECOMMENDED_ITEM_LIST        = "recommended_item_list"
        const val ACTION_REVOKE_QUESTION_DELETED             = "question_deleted"
        const val ACTION_REVOKE_DISCUSSION_DELETED           = "discussion_deleted"
        const val ACTION_REVOKE_REVIEW_DELETED               = "review_deleted"
        const val ACTION_REVOKE_TESTING_REVIEW_DELETED       = "testing_review_deleted"
        const val ACTION_REVOKE_QUESTION_REPLY_DELETED       = "question_reply_deleted"
        const val ACTION_REVOKE_DISCUSSION_REPLY_DELETED     = "discussion_reply_deleted"
        const val ACTION_REVOKE_REVIEW_REPLY_DELETED         = "review_reply_deleted"
        const val ACTION_REVOKE_TESTING_REVIEW_REPLY_DELETED = "testing_review_reply_deleted"
        const val ACTION_REVOKE_TESTING_CONFIRMED_CANCELED   = "testing_confirmed_canceled"
        const val ACTION_REVOKE_ITEM_LIST_DELETED            = "item_list_deleted"
        const val ACTION_REVOKE_ITEM_LIST_REPLY_DELETED      = "item_list_reply_deleted"

        const val COL_TYPE = "type"
        const val COL_ACTION = "action"
        const val COL_SUBJECT_ID = "subject_id"
        const val COL_DETAIL = "detail"
        const val COL_POINT = "point"
    }

    var id: String = ""

    /**
     * @see TYPE_CONTENT
     * @see TYPE_REPLY
     * @see TYPE_TESTING
     * @see TYPE_ITEM_LIST
     * @see TYPE_SYSTEM
     * @see TYPE_REVOKE
     */
    var type: String = ""

    /**
     * 参考 ACTION_XXX
     */
    var action: String = ""

    var subjectId: String = ""

    /**
     * > 0 增加积分；< 0 减少积分
     */
    var point: Int = 0

    /**
     * 这条积分记录对应的商品的 snapshot
     * 根据 [type] 和 [action] 的不同，会有不同的结构
     */
    var detail: JsonObject = JsonObject()

    var createdAtSecond = 0L

    /**
     * 用来显示的积分描述
     */
    var description: String = ""

    /**
     * 用来显示的创建时间：yyyy.MM.dd
     */
    var createdAt: String = ""

}