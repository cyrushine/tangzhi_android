package com.ifanr.tangzhi.ui.icons

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getColorCompat

class IcDownloadBtn: IconView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setText(R.string.ic_download_btn)
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22f)
        isEnabled = true
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        setTextColor(if (isEnabled)
            context.getColorCompat(R.color.white)
        else
            context.getColorCompat(R.color.base_3a))
    }
}