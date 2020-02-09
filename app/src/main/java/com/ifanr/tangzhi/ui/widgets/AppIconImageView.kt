package com.ifanr.tangzhi.ui.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.ifanr.tangzhi.R

/**
 * 显示圆形的 app icon
 */
class AppIconImageView: AppCompatImageView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setImageResource(R.drawable.ic_launcher_circle)
        scaleType = ScaleType.FIT_XY
    }
}