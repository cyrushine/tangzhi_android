package com.ifanr.android.tangzhi.ui.widgets

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.Dimension
import com.ifanr.android.tangzhi.R
import com.ifanr.android.tangzhi.ext.dp2px
import com.ifanr.android.tangzhi.ui.icons.IconView
import com.ifanr.android.tangzhi.ui.icons.TmSpan

/**
 * 文本 "模范指数"，不用再设置文本
 */
class IndexesTextView: IconView {

    constructor(context: Context?) : super(context) { init() }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init() }

    private fun init() {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        text = SpannableStringBuilder()
            .append(context.getString(R.string.indexes))
            .append(context.getString(R.string.ic_TM_icon),
                TmSpan(ctx = context), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        setPadding(0,
            TmSpan.calPaddingTop(
                textSize
            ), 0, 0)
    }
}