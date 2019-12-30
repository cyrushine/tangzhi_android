package com.ifanr.tangzhi.ui.widgets

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getColorCompat

/**
 * 显示日期时间的文本控件：「1分钟前」，「12月4日 13:08」
 */
class DateTextView: AppCompatTextView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f)
        setTextColor(context.getColorCompat(R.color.base_a8))
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.END
    }

    /**
     *
     */
    fun setDatetime(second: Long = 0, millisecond: Long = 0) {
        text = "12月4日 13:08"
    }
}