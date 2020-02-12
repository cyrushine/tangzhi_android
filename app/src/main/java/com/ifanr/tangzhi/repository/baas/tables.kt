package com.ifanr.tangzhi.repository.baas

import com.ifanr.tangzhi.ext.query
import com.ifanr.tangzhi.model.Settings
import com.minapp.android.sdk.Global
import com.minapp.android.sdk.database.Table
import com.minapp.android.sdk.database.query.Where
import io.reactivex.Single
import java.lang.reflect.Type

/**
 * 这里定义知晓云里所有的数据表
 */

val productTable = Table("product")                          // 产品表

val productComment = Table("comment")                   // 产品评论表

val productPost = Table("product_post")                 // 产品文章表

val voteLog = Table("vote_log")                         // 投票和点赞记录表

val timeline = Table("timeline")                        // 动态表

val messageTable = Table("message")                          // 消息表

// 收藏表
val favorite = Table("favorite")

val userprofileTable = Table("_userprofile")                 // 用户表

val itemList = Table("item_list")                       // 清单

val group = Table("group")                              // 圈子

val productParam = Table("product_param")               // 产品基础参数表

val pointLog = Table("point_log")                       // 积分记录表

val tesetings = Table("tesettings")                     // 配置表

val searchLog = Table("search_log")                     // 搜索记录表

val testing = Table("testing")                          // 众测活动表

val testing_topic = Table("testing_topic")              // 活动标签模板表

val testingLog = Table("testing_log")                   // 众测记录表

val atomCollectionLog = Table("atom_collection_log")    // 活动原子收集记录表

val testingOrder = Table("testing_order")               // 订单表

val setting = SettingsTable()                                      // 配置表


class SettingsTable: Table("settings")

inline fun <reified T> SettingsTable.getValue(key: String, type: Type): Single<T> =
    setting.query<Settings>(
        page = -1,
        pageSize = -1,
        where = Where().apply {
            equalTo(Settings.COL_KEY, key)
        }
    ).map {
        val json = it.data.firstOrNull()?.value
        Global.gson().fromJson<T>(json, type)
    }