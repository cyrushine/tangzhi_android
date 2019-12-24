package com.ifanr.tangzhi.ext

import com.minapp.android.sdk.database.query.Where

fun and(left: Where.() -> Unit, right: Where.() -> Unit): Where =
    Where.and(Where().apply(left), Where().apply(right))

fun or(left: Where.() -> Unit, right: Where.() -> Unit): Where =
    Where.or(Where().apply(left), Where().apply(right))