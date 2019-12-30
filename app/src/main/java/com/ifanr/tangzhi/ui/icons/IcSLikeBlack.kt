package com.ifanr.tangzhi.ui.icons

import android.content.Context
import android.util.AttributeSet
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getColorCompat

/**
 * 点赞按钮：
 * isSelected: true - 红色，false - 黑色
 */
class IcSLikeBlack: IconView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setText(R.string.ic_s_like_black)
        setTextColor(context.getColorCompat(R.color.base_3a))
        isSelected = false
    }

    override fun dispatchSetSelected(selected: Boolean) {
        setTextColor(context.getColorCompat(
            if (selected) R.color.base_red else R.color.base_3a))
    }
}