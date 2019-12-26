package com.ifanr.tangzhi.ui.product.comments.review.widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatEditText
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getColorCompat

class TagInputView: AppCompatEditText {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setBackgroundResource(R.drawable.tag_input_view)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        setTextColor(context.getColorCompat(R.color.base_12))
        setHint(R.string.tag_hint)
        setHintTextColor(context.getColorCompat(R.color.base_a8))
    }
}