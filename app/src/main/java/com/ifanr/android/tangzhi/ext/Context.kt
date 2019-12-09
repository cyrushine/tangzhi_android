package com.ifanr.android.tangzhi.ext

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt

fun Context.getColorCompat(@ColorRes res: Int) = ContextCompat.getColor(this, res)

fun Context.dp2px(dp: Int) = resources.displayMetrics.density.times(dp).roundToInt()