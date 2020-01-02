package com.ifanr.tangzhi.model

import com.ifanr.tangzhi.ext.getSafeString
import com.minapp.android.sdk.database.Record

/**
 * 配置表
 */
data class Settings (
    val key: String,
    val value: String,
    val remark: String
) {

    companion object {
        const val COL_KEY = "key"
        const val COL_VALUE = "value"
        const val COL_REMARK = "remark"
    }

    constructor(record: Record): this (
        key = record.getSafeString(COL_KEY),
        value = record.getSafeString(COL_VALUE),
        remark = record.getSafeString(COL_REMARK)
    )
}