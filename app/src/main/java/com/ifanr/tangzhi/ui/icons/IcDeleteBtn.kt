package com.ifanr.tangzhi.ui.icons

import android.content.Context
import android.util.AttributeSet
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getColorCompat

class IcDeleteBtn: IconView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setText(R.string.ic_delete_btn)
        setTextColor(context.getColorCompat(R.color.base_8a))
    }
}