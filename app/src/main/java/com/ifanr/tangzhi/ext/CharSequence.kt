package com.ifanr.tangzhi.ext

import kotlin.math.ceil

/**
 * 计算字符序列占用了几个字节
 */
val CharSequence.bytes: Long
    get() = map { it.bytes }.sum()