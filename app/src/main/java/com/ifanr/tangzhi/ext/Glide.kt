package com.ifanr.tangzhi.ext

import android.content.Context
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.glide.OPTION_RADIUS
import com.ifanr.tangzhi.ui.widgets.StackImageView

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

fun RequestBuilder<RoundedBitmapDrawable>.into(stackIv: StackImageView) =
    into(stackIv.coverIv)

fun RequestManager.gallery() = asDrawable()
    .error(R.drawable.translucent)
    .fallback(R.drawable.translucent)
    .placeholder(R.drawable.translucent)

fun RequestManager.avatar(): RequestBuilder<RoundedBitmapDrawable> {
    val builder = `as`(RoundedBitmapDrawable::class.java)
        .error(R.drawable.default_avatar)
        .fallback(R.drawable.default_avatar)
        .placeholder(R.drawable.default_avatar)
    builder.options[OPTION_RADIUS] = Int.MAX_VALUE.toFloat()
    return builder
}