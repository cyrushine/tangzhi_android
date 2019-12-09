package com.ifanr.android.tangzhi.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import kotlin.math.ceil

/**
 * 获取状态栏的高度
 */
fun Context.getStatusBarHeight(): Int {
    val resId = resources.getIdentifier(
        "status_bar_height", "dimen", "android")
    return if (resId > 0)
        resources.getDimensionPixelSize(resId)
    else
        // 最后采用 api 25 里配置的状态栏高度 24dp
        ceil(resources.displayMetrics.density * 24.toDouble()).toInt()

}

/**
 * @see [Window.statusBar]
 */
fun Activity.statusBar(whiteText: Boolean, translucent: Boolean = true) {
    window?.statusBar(whiteText = whiteText, translucent = translucent)
}

/**
 * 设置状态栏的背景、颜色
 * @param whiteText true - 白色的文本；false - 黑色的文本
 * @param translucent true - 透明背景
 */
fun Window.statusBar(whiteText: Boolean, translucent: Boolean = true) {
    // 4.4 <= version < 6.0 的沉浸式状态栏；原生 rom 是灰色或灰色渐变，有一点透明度的状态栏
    addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

    if (translucent) setStatusBarBackgroundColor(Color.TRANSPARENT)
    setStatusTextColor(!whiteText)
    setDefaultNavigationColor()
}

/**
 * 单独设置状态栏的字体颜色
 * @param dark true - 黑色；false - 白色
 */
fun Window.setStatusTextColor(dark: Boolean) {
    clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        decorView.systemUiVisibility = if (dark)
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        else
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
    setMiuiStatusBarTextColor(dark)
}

private var miuiSupported = true

/**
 * miui 提供的设置状态栏色系的方案
 */
@SuppressLint("PrivateApi")
private fun Window.setMiuiStatusBarTextColor(darkTheme: Boolean) {
    if (!miuiSupported) return
    try {
        val darkModeFlag: Int
        val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
        val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
        darkModeFlag = field.getInt(layoutParams)
        val extraFlagField = javaClass.getMethod("setExtraFlags",
            Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
        extraFlagField.invoke(this, if (darkTheme) darkModeFlag else 0, darkModeFlag)
    } catch (e: Exception) {
        miuiSupported = false
    }
}

/**
 * 单独设置状态栏的背景色
 * @param color
 * @param window
 */
private fun Window.setStatusBarBackgroundColor(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = color
    }
}

fun Window.setDefaultNavigationColor() {
    setNavigationColor(Color.BLACK)
}

/**
 * 设置导航栏的颜色
 */
fun Window.setNavigationColor(color: Int) {
    clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    navigationBarColor = color
}