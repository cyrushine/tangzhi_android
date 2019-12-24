package com.ifanr.tangzhi.model

data class Page<T> (
    val total: Int,
    val page: Int,
    val pageSize: Int,
    val data: List<T>
)