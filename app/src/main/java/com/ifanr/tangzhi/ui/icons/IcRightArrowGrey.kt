package com.ifanr.tangzhi.ui.icons

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import com.ifanr.tangzhi.R

/**
 * 灰色的右箭头➡️
 */
class IcRightArrowGrey: IconView {

    companion object {
        private val GREY = Color.rgb(216, 216, 216)
    }

    constructor(context: Context?) : super(context) { init() }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init() }

    private fun init() {
        setTextColor(GREY)
        setText(R.string.ic_small_arrow)
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12f)
    }

}