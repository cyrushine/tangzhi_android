package com.ifanr.tangzhi.model

import com.google.gson.JsonObject
import com.ifanr.tangzhi.ext.getSafeArrayByConstruct
import com.ifanr.tangzhi.ext.getSafeBoolean
import com.ifanr.tangzhi.ext.getSafeId
import com.ifanr.tangzhi.ext.getSafeString
import com.minapp.android.sdk.database.Record

/**
 * 产品参数
 */
data class ProductParams (
    override val id: String = "",
    val value: List<ProductParamGroup> = emptyList()
): BaseModel {

    companion object {
        const val COL_VALUE = "value"
    }

    constructor(record: Record) : this (
        id = record.getSafeId(),
        value = record.getSafeArrayByConstruct(COL_VALUE, ProductParamGroup::class.java)
    )

    /**
     * 参数分组
     * {
     *      "key": "基本参数",
     *      "children": [
     *          {
     *              "key": "上市日期",
     *              "value": "2019年08月",
     *              "display": true, // 控制具体参数是否展示
     *          },
     *          {
     *              "key": "出厂系统内核",
     *              "value": "Funtouch OS 9（Android 9）",
     *              "display": false
     *          }
     *     ]
     * }
     */
    data class ProductParamGroup (
        val key: String = "",
        val children: List<ProductParam> = emptyList()
    ) {

        constructor(json: JsonObject) : this (
            key = json.getSafeString(COL_KEY),
            children = json.getSafeArrayByConstruct(COL_CHILDREN, ProductParam::class.java)
        )

        companion object {
            const val COL_KEY = "key"
            const val COL_CHILDREN = "children"
        }
    }

    /**
     * 参数条目
     * {
     *      "key": "上市日期",
     *      "value": "2019年08月",
     *      "display": true, // 控制具体参数是否展示
     * }
     */
    data class ProductParam (
        val key: String = "",
        val value: String = "",

        /**
         * 控制具体参数是否展示
         */
        val display: Boolean = false
    ) {

        companion object {
            const val COL_KEY = "key"
            const val COL_VALUE = "value"
            const val COL_DISPLAY = "display"
        }

        constructor(json: JsonObject): this (
            key = json.getSafeString(COL_KEY),
            value = json.getSafeString(COL_VALUE),
            display = json.getSafeBoolean(COL_DISPLAY)
        )
    }

}