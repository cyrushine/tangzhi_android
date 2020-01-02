package com.ifanr.tangzhi.model

import com.ifanr.tangzhi.ext.getSafeBoolean
import com.ifanr.tangzhi.ext.getSafeString
import com.minapp.android.sdk.database.Record

/**
 * 搜索历史
 */
data class SearchLog (
    val key: String,
    val followed: Boolean? = null,
    val status: String = BaseModel.STATUS_APPROVED
) {

    companion object {

        /**
         * 搜索关键字
         */
        const val COL_KEY = "key"

        /**
         * 是否关注
         * optional
         */
        const val COL_FOLLOWED = "followed"

        /**
         * @see BaseModel.STATUS_APPROVED 已记录
         * @see BaseModel.STATUS_DELETED 已被用户删除
         */
        const val COL_STATUS = "status"
    }

    constructor(record: Record): this (
        key = record.getSafeString(COL_KEY),
        followed = record.getSafeBoolean(COL_FOLLOWED),
        status = record.getSafeString(COL_STATUS)
    )
}