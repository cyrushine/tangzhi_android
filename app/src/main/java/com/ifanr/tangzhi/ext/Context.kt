package com.ifanr.tangzhi.ext

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.ifanr.tangzhi.Const
import java.io.File
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

/**
 * 将图片加入到媒体库
 * @param path 图片的绝对地址
 */
fun Context.addToMediaStore(path: String) {
    if (path.isNotEmpty()) {
        try {
            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues().apply {
                put(MediaStore.Images.Media.DATA, path)
                put(MediaStore.Images.Media.DISPLAY_NAME, File(path).name)
            })
            Log.d(Const.tag, "addToMediaStore, $path, $uri")
        } catch (e: Exception) {
            Log.e(Const.tag, "addToMediaStore fail($path)", e)
        }
    }
}