package com.ifanr.tangzhi.ui.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.InputFilter
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ReplacementSpan
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import com.ifanr.tangzhi.ext.dp2px

/**
 * 文本开头有个「竖线」
 */
open class IndicatorTextView: AppCompatTextView {

    companion object {
        private const val TAG = "ProductSectionText"
    }

    private val filter = InputFilter { source, start, end, _, _, _ ->
        return@InputFilter SpannableStringBuilder()
            .append(RectSpan.TEXT, RectSpan(context, color = indicatorColor),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            .append(source, start, end)
    }

    /**
     * 更改指示器的颜色
     */
    var indicatorColor: Int? = null
        set(value) {
            field = value
            (text as? Spanned)?.also { spanned ->
                spanned.getSpans(0, spanned.length, RectSpan::class.java)?.forEach { it.color = value }
            }
        }


    constructor(context: Context?) : super(context) { init() }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init() }

    private fun init() {
        text = SpannableStringBuilder()
            .append(RectSpan.TEXT, RectSpan(context, color = indicatorColor),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            .append(text)
        filters = arrayOf(filter)
    }


}

/**
 * 绘制一个矩形
 */
private class RectSpan(
    ctx: Context,
    @ColorInt var color: Int? = null
): ReplacementSpan() {

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
        val p = color?.let { Paint(paint).apply { color = it } } ?: paint
        canvas.drawRect(x, ascent, rectWidth.toFloat(), descent, p)
    }
}