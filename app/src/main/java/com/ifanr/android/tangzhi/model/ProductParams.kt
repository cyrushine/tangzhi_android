package com.ifanr.android.tangzhi.model

import com.google.gson.JsonObject
import com.ifanr.android.tangzhi.ext.getAsBoolean
import com.ifanr.android.tangzhi.ext.getAsString
import com.minapp.android.sdk.database.Record
import com.minapp.android.sdk.database.Table

/**
 * 产品参数
 */
class ProductParams: Record {

    companion object {
        const val COL_VALUE = "value"
    }

    constructor(record: Record) : super(record._getTable(), record._getJson())

    val value: List<ProductParamGroup>
        get() = getArray(COL_VALUE, JsonObject::class.java)?.map { ProductParamGroup(it) } ?: emptyList()

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
    class ProductParamGroup (
        private val json: JsonObject
    ) {

        companion object {
            const val COL_KEY = "key"
            const val COL_CHILDREN = "children"
        }

        val key: String
            get() = json.getAsString(COL_KEY) ?: ""

        val children: List<ProductParam>
            get() = json.getAsJsonArray(COL_CHILDREN).map { ProductParam(it as JsonObject) }
    }

    /**
     * 参数条目
     * {
     *      "key": "上市日期",
     *      "value": "2019年08月",
     *      "display": true, // 控制具体参数是否展示
     * }
     */
    class ProductParam (
        private val json: JsonObject
    ) {

        companion object {
            const val COL_KEY = "key"
            const val COL_VALUE = "value"
            const val COL_DISPLAY = "display"
        }

        val key: String
            get() = json.getAsString(COL_KEY) ?: ""

        val value: String
            get() = json.getAsString(COL_VALUE) ?: ""

        /**
         * 控制具体参数是否展示
         */
        val display: Boolean
            get() = json.getAsBoolean(COL_DISPLAY) == true
    }

}