package com.ifanr.tangzhi.model

import com.ifanr.tangzhi.Const

data class PageByOffset<T> (
    val total: Int = 0,
    val offset: Int = 0,
    val limit: Int = 0,
    val data: List<T> = emptyList()
)