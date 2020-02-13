package com.ifanr.tangzhi.model

import com.google.gson.JsonObject
import com.ifanr.tangzhi.ext.getSafeId
import com.ifanr.tangzhi.ext.getSafeLong
import com.ifanr.tangzhi.repository.baas.Tables
import com.minapp.android.sdk.database.Record
import com.minapp.android.sdk.database.Table

/**
 * 动态
 */
class Timeline {

    constructor(record: Record) {
        type = record.getString(COL_TYPE)
        action = record.getString(COL_ACTION)
        id = record.getSafeId()
        createdAt = record.getSafeLong(Record.CREATED_AT)
        try {
            detail = Detail(record.getJsonObject(COL_DETAIL)!!)
        } catch (e: Exception) {}
    }

    constructor()


    companion object {
        const val COL_TYPE = "type"
        const val COL_ACTION = "action"
        const val COL_DETAIL = "detail"

        const val TYPE_PRODUCT = "product"
        const val TYPE_TESTING = "testing"
        const val TYPE_CIRCLE = "circle"

        const val ACTION_REVIEW = "review"
        const val ACTION_QUESTION = "question"
        const val ACTION_ANSWER = "answer"
        const val ACTION_TESTING_REVIEW = "testing_review"
        const val ACTION_DISCUSSION = "discussion"
    }

    /**
     * @see TYPE_PRODUCT
     * @see TYPE_TESTING
     * @see TYPE_CIRCLE
     */
    var type: String? = null

    /**
     * @see ACTION_REVIEW
     * @see ACTION_ANSWER
     * @see ACTION_QUESTION
     * @see ACTION_TESTING_REVIEW
     * @see ACTION_DISCUSSION
     */
    var action: String? = null

    var detail: Detail? = null

    var id: String = ""

    var createdAt: Long = 0


    class Detail {

        constructor(json: JsonObject) {
            try {
                product = Product(Record(Tables.product, json.getAsJsonObject(COL_PRODUCT)))
            } catch (e: Exception) {}
            try {
                comment = Comment(Record(Tables.comment, json.getAsJsonObject(COL_COMMENT)))
            } catch (e: Exception) {}
        }

        constructor()


        companion object {
            const val COL_PRODUCT = "product"
            const val COL_COMMENT = "comment"
        }

        var product: Product? = null
        var comment: Comment? = null
    }
}