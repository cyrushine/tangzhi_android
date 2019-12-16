package com.ifanr.tangzhi.ui.icons

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.ifanr.tangzhi.R

/**
 * 白色的爱心 ❤️
 */
class IcHeartIconWhite: IconView {

    constructor(context: Context?) : super(context) { init() }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init() }

    private fun init() {
        setText(R.string.ic_heart_icon)
        setTextColor(Color.WHITE)
    }
}