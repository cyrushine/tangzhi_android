package com.ifanr.tangzhi.ext

import kotlin.math.ceil

/**
 * 计算字符占用了几个字节
 */
val Char.bytes: Long
    get() = ceil(Integer.toHexString(toInt()).length.toFloat().div(2)).toLong()