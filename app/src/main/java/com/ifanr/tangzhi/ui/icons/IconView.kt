package com.ifanr.tangzhi.ui.icons

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.text.style.ReplacementSpan
import android.util.AttributeSet
import android.util.Log
import android.widget.CompoundButton
import androidx.annotation.*
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getColorStateListCompat
import kotlin.math.roundToInt

private object IconHolder {

    private lateinit var icon: Typeface

    fun createIcon(ctx: Context): Typeface {
        if (!this::icon.isInitialized) {
            icon = Typeface.createFromAsset(ctx.assets, "icon.ttf")
        }
        return icon
    }

}

open class IconView: AppCompatTextView {

    constructor(context: Context?) : super(context) { init() }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init() }

    private fun init() {
        typeface =
            IconHolder.createIcon(context)
    }
}

abstract class IconCompoundButton: CompoundButton {

    constructor(context: Context?) : super(context) { init() }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init() }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) { init() }

    private fun init() {
        typeface = IconHolder.createIcon(context)
    }
}

/**
 * 绘制 "TM" 字样，无需设置对应的字符串（传入空字符串即可）
 * 它的大小是字体大小的 [SIZE_SCALE]
 * 需要预留出 [calPaddingTop] 大小的 paddingTop
 */
class TmSpan(ctx: Context): ReplacementSpan() {

    companion object {
        private const val TAG = "TmSpan"
        private const val SIZE_SCALE = 0.7f
        private const val DOWN_OFFSET = 0.4f

        fun calPaddingTop(@Dimension(unit = Dimension.PX) fontSize: Float): Int {
            return fontSize.times(SIZE_SCALE).times(1.0f - DOWN_OFFSET).roundToInt()
        }
    }

    private val tm = ctx.getString(R.string.ic_TM_icon)

    private val p = Paint().apply {
        typeface = IconHolder.createIcon(ctx)
    }

    private fun initPaint(source: Paint) {
        p.textSize = source.textSize.times(SIZE_SCALE)  // 调整字体大小
        p.color = source.color
    }

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        initPaint(paint)
        return p.measureText(tm).roundToInt()
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,             // it's base line
        bottom: Int,
        paint: Paint
    ) {
        initPaint(paint)

        // 调整 TM 的位置，下移 40% 的高度
        val fm = p.fontMetrics
        val height = fm.bottom - fm.top
        val offset = height.times(DOWN_OFFSET)
        canvas.drawText(tm, x, offset, p)
    }
}

/**
 * 绘制 icon font
 */
class IconSpan (ctx: Context): ReplacementSpan() {

    private val p = Paint().apply {
        typeface = IconHolder.createIcon(ctx)
    }

    private fun initPaint(source: Paint) {
        p.textSize = source.textSize
        p.color = source.color
    }

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        initPaint(paint)
        return if (text != null) p.measureText(text, start, end).roundToInt() else 0
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        initPaint(paint)

        if (text != null) {
            val fm = p.fontMetrics
            val height = fm.bottom - fm.top
            canvas.drawText(text, start, end, x, height, p)
        }
    }
}

class IconDrawable (
    @StringRes iconRes: Int,
    ctx: Context
): Drawable() {

    companion object {
        private const val TAG = "IconDrawable"
    }

    private val icon = ctx.getString(iconRes)

    private val paint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        typeface = IconHolder.createIcon(ctx)
    }

    private var tintList: ColorStateList? = null

    override fun draw(canvas: Canvas) {
        paint.color = tintList?.getColorForState(state, paint.color) ?: paint.color
        paint.textSize = bounds.height().toFloat()
        canvas.drawText(
            icon,
            bounds.left.toFloat(),
            bounds.bottom - paint.fontMetrics.descent,
            paint)
    }

    override fun setAlpha(alpha: Int) {}

    override fun getOpacity(): Int = PixelFormat.UNKNOWN

    override fun setColorFilter(colorFilter: ColorFilter?) {}

    override fun setTintList(tint: ColorStateList?) {
        tintList = tint
    }

    override fun onStateChange(state: IntArray?): Boolean {
        invalidateSelf()
        return true
    }

    override fun isStateful(): Boolean = true
}