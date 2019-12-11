package com.ifanr.android.tangzhi.ui.productparam

data class Params (
    val highlight: List<Param>,
    val all: Map<String, List<Param>>
) {

    data class Param (
        val key: String,
        val value: String
    )
}