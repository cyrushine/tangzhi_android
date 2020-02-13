package com.ifanr.tangzhi.repository.baas

import com.ifanr.tangzhi.BuildConfig
import com.ifanr.tangzhi.ext.query
import com.ifanr.tangzhi.model.Settings
import com.minapp.android.sdk.Global
import com.minapp.android.sdk.database.Table
import com.minapp.android.sdk.database.query.Where
import io.reactivex.Single
import java.lang.reflect.Type

object Tables {

    private const val PRODUCT = "product"
    private const val COMMENT = "comment"
    private const val VOTE_LOG = "vote_log"
    private const val TIME_LINE = "time_line"
    private const val MESSAGE = "message"
    private const val FAVORITE = "favorite"
    private const val USER_PROFILE = "_userprofile"
    private const val ITEM_LIST = "item_list"
    private const val PRODUCT_PARAM = "product_param"
    private const val POINT_LOG = "point_log"
    private const val SEARCH_LOG = "search_log"
    private const val SETTINGS = "settings"

    // 生产环境数据库
    private val productionSchemaIds = mapOf(
        PRODUCT to 66705,
        COMMENT to 66743,
        VOTE_LOG to 66750,
        TIME_LINE to 66751,
        MESSAGE to 66752,
        FAVORITE to 66753,
        USER_PROFILE to 40409,
        ITEM_LIST to 83362,
        PRODUCT_PARAM to 85585,
        POINT_LOG to 88201,
        SEARCH_LOG to 74743,
        SETTINGS to 66754
    )

    // 开发环境数据库
    private val devSchemaIds = mapOf(
        PRODUCT to 69479,
        COMMENT to 69481,
        VOTE_LOG to 69482,
        TIME_LINE to 69474,
        MESSAGE to 69478,
        FAVORITE to 69480,
        USER_PROFILE to 40409,
        ITEM_LIST to 83364,
        PRODUCT_PARAM to 85365,
        POINT_LOG to 88200,
        SEARCH_LOG to 74744,
        SETTINGS to 69484
    )

    private fun getSchemaId(key: String): String =
        (if (BuildConfig.ENV == "dev") devSchemaIds else productionSchemaIds)[key].toString()

    private fun getTable(key: String): Table =
        Table(getSchemaId(key))

    // 产品
    val product by lazy { getTable(PRODUCT) }

    // 评论
    val comment by lazy { getTable(COMMENT) }

    // 投票和点赞
    val voteLog by lazy { getTable(VOTE_LOG) }

    // 动态
    val timeline by lazy { getTable(TIME_LINE) }

    // 消息
    val message by lazy { getTable(MESSAGE) }

    // 收藏和关注
    val favorite by lazy { getTable(FAVORITE) }

    // 用户
    val userprofile by lazy { getTable(USER_PROFILE) }

    // 清单
    val itemList by lazy { getTable(ITEM_LIST) }

    // 产品参数
    val productParam by lazy { getTable(PRODUCT_PARAM) }

    // 积分记录
    val pointLog by lazy { getTable(POINT_LOG) }

    // 搜索历史
    val searchLog by lazy { getTable(SEARCH_LOG) }

    // 配置
    val setting by lazy { SettingsTable(getSchemaId(SETTINGS)) }
}

class SettingsTable(tableName: String) : Table(tableName) {
    fun <T> getValue(key: String, type: Type): Single<T> =
        query<Settings>(
            page = -1,
            pageSize = -1,
            where = Where().apply {
                equalTo(Settings.COL_KEY, key)
            }
        ).map {
            val json = it.data.firstOrNull()?.value
            Global.gson().fromJson<T>(json, type)
        }
}