package com.ifanr.tangzhi.model

import com.ifanr.tangzhi.ext.getSafeBoolean
import com.ifanr.tangzhi.ext.getSafeId
import com.ifanr.tangzhi.ext.getSafeString
import com.minapp.android.sdk.database.Record

/**
 * 点赞/投票
 */
class VoteLog {

    constructor(record: Record) {
        id = record.getSafeId()
        type = record.getSafeString(COL_TYPE)
        subjectId = record.getSafeString(COL_SUBJECT_ID)
        isPositive = record.getSafeBoolean(COL_IS_POSITIVE)
    }

    constructor()


    companion object {
        const val COL_TYPE = "type"
        const val COL_SUBJECT_ID = "subject_id"
        const val COL_IS_POSITIVE = "is_positive"

        const val TYPE_COMMENT = "comment"          // 评论
        const val TYPE_ITEM_LIST = "item_list"      // 清单
    }


    var id = ""

    /**
     * @see TYPE_COMMENT
     * @see TYPE_ITEM_LIST
     */
    var type = ""

    var subjectId = ""

    /**
     * true 点赞
     */
    var isPositive = true

}