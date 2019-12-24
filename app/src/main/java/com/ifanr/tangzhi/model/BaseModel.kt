package com.ifanr.tangzhi.model

interface BaseModel {
    val id: String

    companion object {
        const val STATUS_APPROVED = "approved"  // 已发布
        const val STATUS_PENDING = "pending"    // 未发布
        const val STATUS_DRAFT = "draft"        // 草稿
        const val STATUS_DELETED = "deleted"    // 已删除
    }
}