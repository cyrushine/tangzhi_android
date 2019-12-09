package com.ifanr.android.tangzhi.ui.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ReplacementSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.ifanr.android.tangzhi.ext.dp2px

/**
 * 文本开头有个「竖线」
 */
class ProductSectionText: AppCompatTextView {

    constructor(context: Context?) : super(context) { init() }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init() }

    private fun init() {
        text = SpannableStringBuilder()
            .append(RectSpan.TEXT, RectSpan(context), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            .append(text)
    }
}

/**
 * 绘制一个矩形
 */
private class RectSpan(ctx: Context): ReplacementSpan() {

    companion object {
        const val TEXT = "RectSpan"
    }

    private val rectWidth = ctx.dp2px(3)
    private val paddingRight = ctx.dp2px(8)

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        return rectWidth + paddingRight
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
        val fm = paint.fontMetrics
        val ascent = fm.ascent + y
        val descent = fm.descent + y
        canvas.drawRect(x, ascent, rectWidth.toFloat(), descent, paint)
    }
}