package com.ifanr.tangzhi.model

import com.ifanr.tangzhi.ext.getSafeId
import com.ifanr.tangzhi.ext.getSafeString
import com.ifanr.tangzhi.ext.getSafeStringArray
import com.minapp.android.sdk.database.Record

/**
 * 产品清单：用于展示同一类别的产品，如 2019 苹果发布会产品清单
 */
data class ProductList (

    override val id: String = "",

    /**
     * 清单名称
     */
    val name: String = "",

    /**
     * 清单海报图，用于清单详情页顶部
     * ["http://ifanr.com/image.jpg"]
     */
    val banner: List<String> = emptyList(),

    /**
     * 分享配图
     * ["http://ifanr.com/image.jpg"]
     */
    val shareCover: List<String> = emptyList(),

    /**
     * 清单的对象的 id
     * ["5cadc6f7a82b503279251be8"]
     */
    val items: List<String> = emptyList(),

    /**
     * 背景图，用于清单详情页背景
     * "http://ifanr.com/image.jpg"
     */
    val backgroundImage: String = "",

    /**
     * 主题颜色
     * "#ff0099"
     */
    val theme: String = "",

    /**
     * 清单介绍
     */
    val description: String = "",

    /**
     * 清单头图，用于清单列表展示，从产品头图列表中选择一张
     * "http://ifanr.com/image.jpg"
     */
    val coverImage: String = "",

    /**
     * 清单的来源
     * @see SOURCE_SYSTEM
     * @see SOURCE_USER
     */
    val source: String = "",

    /**
     * 清单的状态
     * @see BaseModel.STATUS_APPROVED
     * @see BaseModel.STATUS_PENDING
     * @see BaseModel.STATUS_DRAFT
     * @see BaseModel.STATUS_DELETED
     */
    val status: String = ""
    ): BaseModel {

    constructor(record: Record): this (
        id = record.getSafeId(),
        name = record.getSafeString(COL_NAME),
        banner = record.getSafeStringArray(COL_BANNER),
        shareCover = record.getSafeStringArray(COL_SHARE_COVER),
        items = record.getSafeStringArray(COL_ITEMS),
        backgroundImage = record.getSafeString(COL_BACKGROUND_IMAGE),
        theme = record.getSafeString(COL_THEME),
        description = record.getSafeString(COL_DESCRIPTION),
        coverImage = record.getSafeString(COL_COVER_IMAGE),
        source = record.getSafeString(COL_SOURCE),
        status = record.getSafeString(COL_STATUS)
    )

    companion object {
        const val SOURCE_SYSTEM = "system"  // 后台录入
        const val SOURCE_USER = "user"      // 用户录入

        const val COL_TYPE = "type"
        const val COL_NAME = "name"
        const val COL_BANNER = "banner"
        const val COL_SHARE_COVER = "share_cover"
        const val COL_ITEMS = "items"
        const val COL_BACKGROUND_IMAGE = "background_image"
        const val COL_THEME = "theme"
        const val COL_DESCRIPTION = "description"
        const val COL_COVER_IMAGE = "cover_image"
        const val COL_SOURCE = "source"
        const val COL_STATUS = "status"
        const val COL_ITEMS_META = "items_meta"
        const val COL_ELEMENT_CONFIG = "element_config"
        const val COL_RELEASED_AT = "released_at"
    }

}