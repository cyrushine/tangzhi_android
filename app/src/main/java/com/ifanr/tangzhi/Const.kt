package com.ifanr.tangzhi

import androidx.paging.PagedList
import com.ifanr.tangzhi.ext.toColorInt

object Const {

    const val BAAS_ID = "89edf6b8cac513a6d140"
    val DEFAULT_PRODUCT_THEME = "#383943".toColorInt()
    const val PRE_FETCH_DISTANCE = 5

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
}