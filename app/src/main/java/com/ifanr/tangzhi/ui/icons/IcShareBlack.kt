package com.ifanr.tangzhi.ui.icons

import android.content.Context
import android.util.AttributeSet
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getColorCompat

/**
 * 黑色的分享按钮
 */
class IcShareBlack: IconView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setText(R.string.ic_share_black)
        setTextColor(context.getColorCompat(R.color.base_12))
    }
}