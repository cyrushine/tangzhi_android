package com.ifanr.tangzhi.ui.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.FloatRange
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.ext.getColorCompat
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.tan

class DraggableIndicator: View {

    companion object {
        // 角度/弧度转换倍数，1 角度对应的弧度
        private const val RADIANS = 0.01745f

        // 旋转角度的最大值
        private const val MAX_DEGREES = 20f

        private const val TAG = "DraggableIndicator"
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColorCompat(R.color.base_da)
        style = Paint.Style.FILL
    }

    // 线段的高度
    private val indicatorHeight = context.dp2px(4f)

    // 矩形圆角半径
    private val radius = indicatorHeight / 2

    // 左右两个需要空出一定的控件
    private val paddingLeftRight = indicatorHeight

    // 旋转的程度
    private var percent = 0f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val maxIndicatorWidth = calIndicatorWidth(MAX_DEGREES)
        val heightNeeded = tan(MAX_DEGREES * RADIANS) * maxIndicatorWidth * 2f
        setMeasuredDimension(measuredWidth, heightNeeded.roundToInt())

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 当前旋转的角度
        val degrees = MAX_DEGREES * percent
        // 线段的长度（左右个一条）
        val indicatorWidth = calIndicatorWidth(degrees)

        canvas.save()
        canvas.rotate(degrees)
        canvas.drawRoundRect(
            paddingLeftRight,
            0f,
            paddingLeftRight + indicatorWidth,
            indicatorHeight,
            radius, radius, paint)
        canvas.restore()

        canvas.save()
        canvas.rotate(-degrees, measuredWidth.toFloat(), 0f)
        canvas.drawRoundRect(
            measuredWidth - paddingLeftRight - indicatorWidth,
            0f,
            measuredWidth - paddingLeftRight,
            indicatorHeight,
            radius, radius, paint)
        canvas.restore()
    }

    /**
     * 计算出线段的长度（左右个一条）
     * @param degrees 角度（0 - 360）
     */
    private fun calIndicatorWidth(@FloatRange(from = 0.0, to = 360.0) degrees: Float): Float {
        var indicatorWidth = (measuredWidth - 2 * paddingLeftRight) / 2f + radius
        if (degrees > 0) {
            indicatorWidth /= cos(degrees * RADIANS)
        }
        return indicatorWidth
    }

    fun setRotatePercent(@FloatRange(from = 0.0, to = 1.0) percent: Float) {
        this.percent = percent
        invalidate()
    }
}