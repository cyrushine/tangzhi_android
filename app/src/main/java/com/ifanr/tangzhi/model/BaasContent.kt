package com.ifanr.tangzhi.model

import com.ifanr.tangzhi.ext.getSafeString
import com.minapp.android.sdk.content.Content

/**
 * BaaS 的内容
 */
data class BaasContent (
    val title: String = "",
    val descroption: String = "",
    val coverImage: String = "",
    val content: String = ""
) {

    constructor(record: Content): this(
        title = record.getSafeString(Content.TITLE),
        descroption = record.getSafeString(Content.DESCRIPTION),
        coverImage = record.getSafeString(Content.COVER),
        content = record.getSafeString(Content.CONTENT)
    )

}