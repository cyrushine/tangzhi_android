package com.ifanr.android.tangzhi.ui.widgets

import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape

/**
 * 完全的圆角矩形
 */
class CompleteRoundedRectDrawable: ShapeDrawable() {

    init {
        shape = RoundRectShape(FloatArray(8) { Float.MAX_VALUE }, null, null)
    }
}