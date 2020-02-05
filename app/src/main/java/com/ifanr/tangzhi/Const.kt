package com.ifanr.tangzhi

import androidx.paging.PagedList
import com.ifanr.tangzhi.ext.toColorInt

object Const {

    const val BAAS_ID = "89edf6b8cac513a6d140"
    val DEFAULT_PRODUCT_THEME = "#383943".toColorInt()
    const val PRE_FETCH_DISTANCE = 5

    // 糖纸的微信 app id
    const val WECHAT_APP_ID = "wx0479d25aff361645"

    // 糖纸小程序 user name
    const val TANGZHI_USER_NAME = "gh_ce46f0d4793b"

    // 糖纸小程序，产品页路径
    const val TANGZHI_PRODUCT_PATH =
        "packages/product/pages/product-hardware-detail/product-hardware-detail?id="

    // 相关产品最多展示 6 个
    const val PRODUCT_RELATED_MAX = 6
    const val PAGE_SIZE = 15
    const val WEIBO_APP_KEY = "677122627"
    val pagedListConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setInitialLoadSizeHint(PAGE_SIZE)
        .setPageSize(PAGE_SIZE)
        .setMaxSize(Int.MAX_VALUE)
        .setPrefetchDistance(PRE_FETCH_DISTANCE)
        .build()

    const val privacyPolicyUri = "https://www.ifanr.com/privacypolicy"
    const val userAgreementUri = "https://www.ifanr.com/privacypolicy"
}