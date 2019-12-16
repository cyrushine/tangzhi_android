package com.ifanr.tangzhi.ui.product.indexes

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import com.airbnb.epoxy.ModelView
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getColorCompat
import com.ifanr.tangzhi.ext.setPadding
import com.ifanr.tangzhi.ui.widgets.IndicatorTextView

abstract class BaseGroupHeader: IndicatorTextView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        indicatorColor = context.getColorCompat(R.color.base_d8)
        setTextColor(context.getColorCompat(R.color.base_12))
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        typeface = Typeface.DEFAULT_BOLD
        setPadding(intArrayOf(0, 25, 0, 13))
    }
}

@ModelView(autoLayout = ModelView.Size.WRAP_WIDTH_WRAP_HEIGHT)
class UserGroupHeader: BaseGroupHeader {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setText(R.string.proodict_indexes_dialog_user)
    }
}

@ModelView(autoLayout = ModelView.Size.WRAP_WIDTH_WRAP_HEIGHT)
class OrgGroupHeader: BaseGroupHeader {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setText(R.string.proodict_indexes_dialog_org)
    }
}

@ModelView(autoLayout = ModelView.Size.WRAP_WIDTH_WRAP_HEIGHT)
class ThirdPartyGroupHeader: BaseGroupHeader {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setText(R.string.proodict_indexes_dialog_third_party)
    }
}