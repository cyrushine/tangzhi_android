package com.ifanr.tangzhi.ext

import android.util.Log
import androidx.core.widget.NestedScrollView

/**
 * 是否可以继续往下滚动
 */
fun NestedScrollView.canScrollDown(): Boolean {
    if (childCount > 0) {
        val scrollableHeight = getChildAt(0).measuredHeight - measuredHeight
        return scrollY < scrollableHeight
    }
    return false
}