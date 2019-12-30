package com.ifanr.tangzhi.ui.widgets

import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import androidx.annotation.ColorInt

/**
 * 圆滑的矩形（左右是半圆）
 */
class RoundCapRectDrawable (
    @ColorInt color: Int
) : ShapeDrawable(RoundRectShape(FloatArray(8) { Float.MAX_VALUE }, null, null)) {

    init {
        paint.color = color
    }
}