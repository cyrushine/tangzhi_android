package com.ifanr.android.tangzhi.glide

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.load.Option
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableResource
import com.bumptech.glide.load.resource.drawable.DrawableResource
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder
import com.bumptech.glide.util.Util
import com.ifanr.android.tangzhi.ext.dp2px

val OPTION_RADIUS = Option.memory<Float>("option_radius")

/**
 * 圆角图片，可以用 [OPTION_RADIUS] 控制圆角的大小
 */
class RoundedBitmapDrawableTranscoder (
    ctx: Context,
    private val bitmapPool: BitmapPool
): ResourceTranscoder<Bitmap, RoundedBitmapDrawable> {

    private val defaultRadius = ctx.dp2px(5).toFloat()
    private val resource = ctx.resources

    override fun transcode(
        toTranscode: Resource<Bitmap>,
        options: Options
    ): Resource<RoundedBitmapDrawable>? {

        val radius = options.get(OPTION_RADIUS) ?: defaultRadius
        val drawable = RoundedBitmapDrawableFactory.create(resource,
            toTranscode.get()).apply { cornerRadius = radius }
        return RoundedBitmapDrawableResource(drawable, bitmapPool)
    }
}

/**
 * @see [BitmapDrawableResource]
 */
private class RoundedBitmapDrawableResource (
    drawable: RoundedBitmapDrawable,
    private val pool: BitmapPool
): DrawableResource<RoundedBitmapDrawable>(drawable) {

    override fun getResourceClass(): Class<RoundedBitmapDrawable> {
        return RoundedBitmapDrawable::class.java
    }

    override fun getSize(): Int {
        return drawable.bitmap?.let { Util.getBitmapByteSize(it) } ?: 0
    }

    override fun recycle() {
        drawable.bitmap?.also { pool.put(it) }
    }

    override fun initialize() {
        drawable.bitmap?.prepareToDraw()
    }
}