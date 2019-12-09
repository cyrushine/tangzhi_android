package com.ifanr.android.tangzhi.ext

import androidx.core.graphics.drawable.RoundedBitmapDrawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.ifanr.android.tangzhi.R
import com.ifanr.android.tangzhi.glide.OPTION_RADIUS

fun RequestManager.default() = asBitmap()
    .error(R.drawable.image_placeholder)
    .fallback(R.drawable.image_placeholder)
    .placeholder(R.drawable.image_placeholder)

fun RequestManager.roundedRect() = `as`(RoundedBitmapDrawable::class.java)
    .error(R.drawable.image_placeholder)
    .fallback(R.drawable.image_placeholder)
    .placeholder(R.drawable.image_placeholder)

fun RequestManager.rounded(): RequestBuilder<RoundedBitmapDrawable> {
    val builder = `as`(RoundedBitmapDrawable::class.java)
        .error(R.drawable.image_placeholder)
        .fallback(R.drawable.image_placeholder)
        .placeholder(R.drawable.image_placeholder)
    builder.options[OPTION_RADIUS] = Int.MAX_VALUE.toFloat()
    return builder
}