package com.ifanr.android.tangzhi.ui.productparam.widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.ifanr.android.tangzhi.R
import com.ifanr.android.tangzhi.ext.getColorCompat
import com.ifanr.android.tangzhi.ui.widgets.IndicatorTextView

abstract class BaseHeader: AppCompatTextView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        setTextColor(Color.WHITE)
    }
}

@ModelView(defaultLayout = R.layout.product_param_highlight_header)
class HighlightHeader:
    BaseHeader {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setText(R.string.product_param_highlight)
    }
}

@ModelView(defaultLayout = R.layout.product_param_all_header)
class AllHeader: BaseHeader {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setText(R.string.product_param_all)
    }
}

@ModelView(defaultLayout = R.layout.product_param_all_group_header)
class AllGroupHeader: IndicatorTextView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        setTextColor(Color.WHITE)
        indicatorColor = context.getColorCompat(R.color.base_red)
    }

    @TextProp
    fun setContent(text: CharSequence) {
        setText(text)
    }
}