package com.ifanr.tangzhi.repository.baas.datasource

import android.content.Context
import com.google.gson.JsonObject
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getSafeString
import com.ifanr.tangzhi.model.PointLog
import com.ifanr.tangzhi.repository.baas.Tables
import com.ifanr.tangzhi.util.uuid
import com.minapp.android.sdk.database.Record
import com.minapp.android.sdk.database.query.Where
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

class PointLogDataSource (
    private val ctx: Context,
    val userId: Long,
    val type: String? = ""
): BaseDataSource<PointLog>(
    table = Tables.pointLog,
    clz = PointLog::class.java,
    initQuery = {
        put(Where().apply {
            equalTo(Record.CREATED_BY, userId)
            if (!type.isNullOrEmpty()) {
                equalTo(PointLog.COL_TYPE, type)
            }
        })
    }
) {

    private val formatter by lazy {
        DateTimeFormatter.ofPattern("yyyy.MM.dd", Locale.getDefault())
    }

    override fun doOnNext(elem: PointLog) {
        // 积分操作类型
        val type = getTypeName(elem.type)

        // 产品名称
        var productName = ""
        try {
            productName =
                elem.detail.getAsJsonObject("product").getSafeString("name")
        } catch (e: Exception) {}

        if (productName.isEmpty()) {
            try {
                productName =
                    elem.detail.getAsJsonObject("testing").getSafeString("name")
            } catch (e: Exception) {}
        }

        if (productName.isEmpty()) {
            try {
                productName =
                    elem.detail.getAsJsonObject("itemList").getSafeString("name")
            } catch (e: Exception) {}
        }

        // 积分操作
        val action = getAction(elem.type, elem.action)

        elem.description = when {
            (elem.type == PointLog.TYPE_ITEM_LIST && elem.action == PointLog.ACTION_ITEM_LIST_CREATE) ||
                    (elem.type == PointLog.TYPE_REPLY && elem.action == PointLog.ACTION_REPLY_ITEM_LIST) ||
                    (elem.type == PointLog.TYPE_SYSTEM && elem.action == PointLog.ACTION_SYSTEM_RECOMMENDED_ITEM_LIST) ||
                    (elem.type == PointLog.TYPE_SYSTEM && elem.action == PointLog.ACTION_SYSTEM_REWARD) ||
                    (elem.type == PointLog.TYPE_REVOKE && elem.action == PointLog.ACTION_REVOKE_ITEM_LIST_DELETED) ||
                    (elem.type == PointLog.TYPE_REVOKE && elem.action == PointLog.ACTION_REVOKE_ITEM_LIST_REPLY_DELETED)
            -> "${type}_${action}"

            elem.type == PointLog.TYPE_REVOKE -> "${type}_删除${productName}${action.substring(2)}"

            else -> "${type}_${productName}${action}"
        }

        elem.createdAt = LocalDateTime.ofEpochSecond(
            elem.createdAtSecond, 0, ZoneOffset.ofHours(8)).format(formatter)
    }

    private fun getTypeName(type: String) = ctx.getString(when (type) {
        PointLog.TYPE_REPLY -> R.string.points_record_type_reply
        PointLog.TYPE_TESTING -> R.string.points_record_type_testing
        PointLog.TYPE_ITEM_LIST -> R.string.points_record_type_item_list
        PointLog.TYPE_SYSTEM -> R.string.points_record_type_system
        PointLog.TYPE_REVOKE -> R.string.points_record_type_revoke
        else -> R.string.points_record_type_content
    })

    private fun getAction(type: String, action: String) = when ("${type}_${action}") {
        "${PointLog.TYPE_CONTENT}_${PointLog.ACTION_CONTENT_REVIEW}" -> "点评"
        "${PointLog.TYPE_CONTENT}_${PointLog.ACTION_CONTENT_DISCUSSION}" -> "帖子"
        "${PointLog.TYPE_CONTENT}_${PointLog.ACTION_CONTENT_QUESTION}" -> "提问"
        "${PointLog.TYPE_CONTENT}_${PointLog.ACTION_CONTENT_TESTING_REVIEW}" -> "评测"

        "${PointLog.TYPE_REPLY}_${PointLog.ACTION_REPLY_REVIEW}" -> "回复点评"
        "${PointLog.TYPE_REPLY}_${PointLog.ACTION_REPLY_DISCUSSION}" -> "回复帖子"
        "${PointLog.TYPE_REPLY}_${PointLog.ACTION_REPLY_QUESTION}" -> "回复提问"
        "${PointLog.TYPE_REPLY}_${PointLog.ACTION_REPLY_TESTING_REVIEW}" -> "回复评测"
        "${PointLog.TYPE_REPLY}_${PointLog.ACTION_REPLY_ITEM_LIST}" -> "清单评论"

        "${PointLog.TYPE_TESTING}_${PointLog.ACTION_TESTING_RESERVED}" -> "预约"
        "${PointLog.TYPE_TESTING}_${PointLog.ACTION_TESTING_ASSISTED}" -> "助力"
        "${PointLog.TYPE_TESTING}_${PointLog.ACTION_TESTING_APPLYING}" -> "开启申请"
        "${PointLog.TYPE_TESTING}_${PointLog.ACTION_TESTING_APPLIED}" -> "集齐元素"
        "${PointLog.TYPE_TESTING}_${PointLog.ACTION_TESTING_SURVEY_FILLED}" -> "填写调查问卷"
        "${PointLog.TYPE_TESTING}_${PointLog.ACTION_TESTING_CONFIRMED}" -> "获得众测资格"

        "${PointLog.TYPE_ITEM_LIST}_${PointLog.ACTION_ITEM_LIST_CREATE}" -> "创建清单"

        "${PointLog.TYPE_SYSTEM}_${PointLog.ACTION_SYSTEM_RECOMMENDED_REVIEW}" -> "精选点评"
        "${PointLog.TYPE_SYSTEM}_${PointLog.ACTION_SYSTEM_RECOMMENDED_DISCUSSION}" -> "精选帖子"
        "${PointLog.TYPE_SYSTEM}_${PointLog.ACTION_SYSTEM_RECOMMENDED_QUESTION}" -> "精选提问"
        "${PointLog.TYPE_SYSTEM}_${PointLog.ACTION_SYSTEM_RECOMMENDED_ANSWER}" -> "精选回答"
        "${PointLog.TYPE_SYSTEM}_${PointLog.ACTION_SYSTEM_TOPPING_QUESTION}" -> "置顶提问"
        "${PointLog.TYPE_SYSTEM}_${PointLog.ACTION_SYSTEM_TOPPING_DISCUSSION}" -> "置顶帖子"
        "${PointLog.TYPE_SYSTEM}_${PointLog.ACTION_SYSTEM_REWARD}" -> "系统发放的奖励"
        "${PointLog.TYPE_SYSTEM}_${PointLog.ACTION_SYSTEM_RECOMMENDED_ITEM_LIST}" -> "精选清单"

        "${PointLog.TYPE_REVOKE}_${PointLog.ACTION_REVOKE_QUESTION_DELETED}" -> "删除提问"
        "${PointLog.TYPE_REVOKE}_${PointLog.ACTION_REVOKE_DISCUSSION_DELETED}" -> "删除帖子"
        "${PointLog.TYPE_REVOKE}_${PointLog.ACTION_REVOKE_REVIEW_DELETED}" -> "删除点评"
        "${PointLog.TYPE_REVOKE}_${PointLog.ACTION_REVOKE_TESTING_REVIEW_DELETED}" -> "删除评测"
        "${PointLog.TYPE_REVOKE}_${PointLog.ACTION_REVOKE_QUESTION_REPLY_DELETED}" -> "删除问答回复"
        "${PointLog.TYPE_REVOKE}_${PointLog.ACTION_REVOKE_DISCUSSION_REPLY_DELETED}" -> "删除讨论回复"
        "${PointLog.TYPE_REVOKE}_${PointLog.ACTION_REVOKE_REVIEW_REPLY_DELETED}" -> "删除点评回复"
        "${PointLog.TYPE_REVOKE}_${PointLog.ACTION_REVOKE_TESTING_REVIEW_REPLY_DELETED}" -> "删除评测回复"
        "${PointLog.TYPE_REVOKE}_${PointLog.ACTION_REVOKE_TESTING_CONFIRMED_CANCELED}" -> "取消众测资格"
        "${PointLog.TYPE_REVOKE}_${PointLog.ACTION_REVOKE_ITEM_LIST_DELETED}" -> "删除清单"
        "${PointLog.TYPE_REVOKE}_${PointLog.ACTION_REVOKE_ITEM_LIST_REPLY_DELETED}" -> "删除清单评论"

        else -> ""
    }
}