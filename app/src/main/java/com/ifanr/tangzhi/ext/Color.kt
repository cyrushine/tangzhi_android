package com.ifanr.tangzhi.ext

import androidx.annotation.ColorInt

@ColorInt
fun colorRGBA(red: Float, green: Float, blue: Float, alpha: Float): Int {
    return (alpha * 255f + 0.5f).toInt() shl 24 or
            ((red * 255f + 0.5f).toInt() shl 16) or
            ((green * 255f + 0.5f).toInt() shl 8) or
            (blue * 255f + 0.5f).toInt()
}