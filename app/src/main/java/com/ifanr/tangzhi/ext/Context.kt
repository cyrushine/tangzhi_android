package com.ifanr.tangzhi.ext

import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt

fun Context.getColorCompat(@ColorRes res: Int) = ContextCompat.getColor(this, res)

fun Context.getColorStateListCompat(@ColorRes res: Int) =
    ContextCompat.getColorStateList(this, res)

fun Context.dp2px(dp: Int) = resources.displayMetrics.density.times(dp).roundToInt()

fun Context.dp2px(dp: Float) = resources.displayMetrics.density.times(dp)

fun Context.appLabelString(): String {
    return try {
        getString(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA).labelRes)
    } catch (e: Exception) { "" }
}

val Context.appName: String
    get() = appLabelString()