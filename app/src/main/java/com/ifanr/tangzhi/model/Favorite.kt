package com.ifanr.tangzhi.model

import com.ifanr.tangzhi.ext.getSafeBoolean
import com.ifanr.tangzhi.ext.getSafeString
import com.minapp.android.sdk.database.Record

/**
 * 收藏表
 */
class Favorite {

    companion object {
        const val COL_TYPE = "type"
        const val COL_ACTION = "action"
        const val COL_SUBJECT_ID = "subject_id"
        const val COL_VISIBLE = "visible"

        const val TYPE_HARDWARE = "hardware"
        const val TYPE_ITEM_LIST = "item_list"

        const val ACTION_FAVORITE = "favorite"  // 收藏
        const val ACTION_FOLLOW = "follow"      // 关注
    }

    constructor(record: Record) {
        type = record.getSafeString(COL_TYPE)
        action = record.getSafeString(COL_ACTION)
        subjectId = record.getSafeString(COL_SUBJECT_ID)
        visible = record.getSafeBoolean(COL_VISIBLE)
    }

    constructor()


    /**
     * @see TYPE_HARDWARE
     * @see TYPE_ITEM_LIST
     */
    var type: String = ""

    /**
     * @see ACTION_FAVORITE
     * @see ACTION_FOLLOW
     */
    var action: String = ""

    var subjectId: String = ""

    /**
     * 是否在收藏列表可见
     */
    var visible: Boolean = true

}