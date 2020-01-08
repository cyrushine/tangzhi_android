package com.ifanr.tangzhi.ext

import android.content.res.Resources
import android.util.TypedValue
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

fun View.setSelectableItemBackground() {
    val out = TypedValue()
    if(context.theme.resolveAttribute(android.R.attr.selectableItemBackground, out, true)) {
        try {
            background = context.getDrawable(out.resourceId)
            isClickable = true
        } catch (e: Resources.NotFoundException) {}
    }
}