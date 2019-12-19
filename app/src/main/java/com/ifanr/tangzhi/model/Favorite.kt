package com.ifanr.tangzhi.model

import com.ifanr.tangzhi.ext.getSafeString
import com.minapp.android.sdk.database.Record

class Favorite(record: Record): Record(record._getTable(), record._getJson()) {

    companion object {
        const val TYPE_HARDWARE = "hardware"    // 硬件产品

        const val COL_TYPE = "type"
        const val COL_SUBJECT_ID = "subject_id"
    }

    /**
     * 收藏类型
     * @see TYPE_HARDWARE
     */
    var type: String
        get() = getSafeString(COL_TYPE)
        set(value) { put(COL_TYPE, value) }

    /**
     * 收藏对象的 id，用于查询用户的收藏状态
     */
    var subjectId: String
        get() = getSafeString(COL_SUBJECT_ID)
        set(value) { put(COL_SUBJECT_ID, value) }
}