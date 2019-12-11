package com.ifanr.android.tangzhi.ext

import android.view.View

/**
 * @param paddings - 4 elements in dp
 */
fun View.setPadding(paddings: IntArray) {
    setPadding(
        context.dp2px(paddings[0]),
        context.dp2px(paddings[1]),
        context.dp2px(paddings[2]),
        context.dp2px(paddings[3]))
}