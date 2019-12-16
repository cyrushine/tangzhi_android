package com.ifanr.tangzhi.ui.product

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import com.ifanr.tangzhi.Const
import kotlin.math.roundToInt

class CoverImageView: AppCompatImageView {

    companion object {
        private const val MASK_RATIO = 14        // mask 的宽高比
        private const val VIEW_RATIO = 1.3345f   // 整个 view 的宽高比
    }

    private lateinit var maskDrawable: GradientDrawable

    constructor(context: Context?) : super(context) { init() }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init() }

    private fun init() {
        maskDrawable = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(Color.TRANSPARENT, Const.DEFAULT_PRODUCT_THEME))
        scaleType = ScaleType.CENTER_CROP
    }

    override fun onDrawForeground(canvas: Canvas) {
        val maskHeight = measuredWidth / MASK_RATIO
        maskDrawable.setBounds(0, measuredHeight - maskHeight, measuredWidth, measuredHeight)
        maskDrawable.draw(canvas)
        super.onDrawForeground(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredWidth.div(VIEW_RATIO).roundToInt())
    }

    fun setThemeColor(@ColorInt color: Int) {
        maskDrawable.colors = intArrayOf(Color.TRANSPARENT, color)
        invalidate()
    }
}