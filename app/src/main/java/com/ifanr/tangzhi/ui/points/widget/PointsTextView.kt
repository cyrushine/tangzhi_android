package com.ifanr.tangzhi.ui.points.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getColorCompat

/**
 * 积分变动文本控件
 */
class PointsTextView: AppCompatTextView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
        typeface = Typeface.DEFAULT_BOLD
    }

    fun setPoints(points: Int) {

        text = when {
            points > 0 -> "+$points"
            else -> "$points"
        }

        setTextColor(context.getColorCompat(
            if (points > 0) R.color.base_gold else R.color.base_3a))
    }
}