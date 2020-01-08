package com.ifanr.tangzhi.ui.index.profile.wdiget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getColorCompat
import com.ifanr.tangzhi.ext.inflateInto
import com.ifanr.tangzhi.ext.setPadding
import com.ifanr.tangzhi.ui.icons.IconView
import com.ifanr.tangzhi.ui.widgets.RoundedRectDrawable

/**
 * 「糖纸积分」文本
 */
class PointText: ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        inflateInto(R.layout.point_text)
    }
}