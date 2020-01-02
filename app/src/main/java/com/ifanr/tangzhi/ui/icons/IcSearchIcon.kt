package com.ifanr.tangzhi.ui.icons

import android.content.Context
import android.util.AttributeSet
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getColorCompat

class IcSearchIcon: IconView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setText(R.string.ic_search_icon)
        setTextColor(context.getColorCompat(R.color.base_df))
    }
}