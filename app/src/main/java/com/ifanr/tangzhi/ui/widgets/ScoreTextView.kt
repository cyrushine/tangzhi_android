package com.ifanr.tangzhi.ui.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.FloatRange
import androidx.appcompat.widget.AppCompatTextView
import java.math.RoundingMode
import java.text.NumberFormat

/**
 * 评分控件
 * 会渲染为：8.6, 0.0, ... 这样规格化的字符串
 */
class ScoreTextView: AppCompatTextView {

    private val formater by lazy {
        NumberFormat.getNumberInstance().apply {
            roundingMode = RoundingMode.FLOOR
            maximumFractionDigits = 1
            maximumIntegerDigits = 1
            minimumFractionDigits = 1
            minimumIntegerDigits = 1
        }
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    fun setScore(@FloatRange(from = 0.0, to = 10.0) score: Float) {
        text = formater.format(score.coerceIn(0f, 10f))
    }
}