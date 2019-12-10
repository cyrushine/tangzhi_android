package com.ifanr.android.tangzhi.model

import com.ifanr.android.tangzhi.model.Product.Companion.STATUS_APPROVED
import com.ifanr.android.tangzhi.model.Product.Companion.STATUS_DELETED
import com.ifanr.android.tangzhi.model.Product.Companion.STATUS_DRAFT
import com.ifanr.android.tangzhi.model.Product.Companion.STATUS_PENDING
import com.ifanr.android.tangzhi.repository.itemList
import com.minapp.android.sdk.database.Record

/**
 * 产品清单：用于展示同一类别的产品，如 2019 苹果发布会产品清单
 * @see [itemList]
 */
class ProductList: Record {

    constructor(base: Record): super(base._getTable(), base._getJson())

    companion object {
        const val SOURCE_SYSTEM = "system"  // 后台录入
        const val SOURCE_USER = "user"      // 用户录入

        const val STATUS_APPROVED = "approved"  // 已发布
        const val STATUS_PENDING = "pending"    // 未发布
        const val STATUS_DRAFT = "draft"        // 草稿
        const val STATUS_DELETED = "deleted"    // 已删除

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

    /**
     * 清单名称
     */
    val name: String?
        get() = getString(COL_NAME)

    /**
     * 清单海报图，用于清单详情页顶部
     * ["http://ifanr.com/image.jpg"]
     */
    val banner: List<String>?
        get() = getArray(COL_BANNER, String::class.java)

    /**
     * 分享配图
     * ["http://ifanr.com/image.jpg"]
     */
    val shareCover: List<String>?
        get() = getArray(COL_SHARE_COVER, String::class.java)

    /**
     * 清单的对象的 id
     * ["5cadc6f7a82b503279251be8"]
     */
    val items: List<String>?
        get() = getArray(COL_ITEMS, String::class.java)

    /**
     * 背景图，用于清单详情页背景
     * "http://ifanr.com/image.jpg"
     */
    val backgroundImage: String?
        get() = getString(COL_BACKGROUND_IMAGE)

    /**
     * 主题颜色
     * "#ff0099"
     */
    val theme: String?
        get() = getString(COL_THEME)

    /**
     * 清单介绍
     */
    val description: String?
        get() = getString(COL_DESCRIPTION)

    /**
     * 清单头图，用于清单列表展示，从产品头图列表中选择一张
     * "http://ifanr.com/image.jpg"
     */
    val coverImage: String?
        get() = getString(COL_COVER_IMAGE)

    /**
     * 清单的来源
     * @see SOURCE_SYSTEM
     * @see SOURCE_USER
     */
    val source: String?
        get() = getString(COL_SOURCE)

    /**
     * 清单的状态
     * @see STATUS_APPROVED
     * @see STATUS_PENDING
     * @see STATUS_DRAFT
     * @see STATUS_DELETED
     */
    val status: String?
        get() = getString(COL_STATUS)

}