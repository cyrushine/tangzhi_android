package com.ifanr.android.tangzhi.ui.icons

import android.content.Context
import android.util.AttributeSet
import com.ifanr.android.tangzhi.R

/**
 * 左箭头⬅️
 */
class IcLeftArrow: IconView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setText(R.string.ic_b_arrow)
    }
}