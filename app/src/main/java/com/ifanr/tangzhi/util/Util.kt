package com.ifanr.tangzhi.util

import androidx.core.view.NestedScrollingParent3
import androidx.core.view.ViewCompat
import java.util.*


fun uuid() = UUID.randomUUID().toString()

fun uuidLong() = UUID.randomUUID().mostSignificantBits

/**
 * @see NestedScrollingParent3
 */
fun axesToString(axes: Int): String {
    val vertical = axes.and(ViewCompat.SCROLL_AXIS_VERTICAL) == ViewCompat.SCROLL_AXIS_VERTICAL
    val horizontal = axes.and(ViewCompat.SCROLL_AXIS_HORIZONTAL) == ViewCompat.SCROLL_AXIS_HORIZONTAL
    return when {
        vertical && horizontal -> "SCROLL_AXIS_VERTICAL && SCROLL_AXIS_HORIZONTAL"
        vertical -> "SCROLL_AXIS_VERTICAL"
        horizontal -> "SCROLL_AXIS_HORIZONTAL"
        else -> "NONE"
    }
}

/**
 * @see NestedScrollingParent3
 */
fun typeToString(type: Int): String = when (type) {
    ViewCompat.TYPE_TOUCH -> "TOUCH"
    ViewCompat.TYPE_NON_TOUCH -> "NON TOUCH"
    else -> "NONE"
}