package com.ifanr.tangzhi.model

import com.ifanr.tangzhi.Const

data class Page<T> (
    val total: Int = 0,
    val page: Int = 0,
    val pageSize: Int = Const.PAGE_SIZE,
    val data: List<T> = emptyList()
)