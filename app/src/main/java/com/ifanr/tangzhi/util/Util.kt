package com.ifanr.tangzhi.util

import androidx.core.view.NestedScrollingParent3
import androidx.core.view.ViewCompat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import java.util.*
import java.util.concurrent.TimeUnit


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

val AppGson: Gson by lazy {
    GsonBuilder()
        .create()
}

/**
 * count down with [from .. 0]
 * @param periodMs 毫秒
 */
fun countDown(from: Long, periodMs: Long = 1000L) =
    Observable.intervalRange(0, from + 1, 0, periodMs, TimeUnit.MILLISECONDS)
        .map { from - it }