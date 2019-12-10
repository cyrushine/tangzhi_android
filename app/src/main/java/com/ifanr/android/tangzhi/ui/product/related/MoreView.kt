package com.ifanr.android.tangzhi.ui.product.related

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.epoxy.ModelView
import com.ifanr.android.tangzhi.R
import com.ifanr.android.tangzhi.ext.dp2px
import com.ifanr.android.tangzhi.ext.getColorCompat
import com.ifanr.android.tangzhi.ui.widgets.CompleteRoundedRectDrawable

/**
 * 查看更多
 */
@ModelView(autoLayout = ModelView.Size.WRAP_WIDTH_WRAP_HEIGHT)
class MoreView: AppCompatTextView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setText(R.string.product_related_product_more)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f)
        setTextColor(context.getColorCompat(R.color.product_related_more_text))
        setPadding(context.dp2px(11), context.dp2px(16), context.dp2px(11),
            context.dp2px(16))
        background = CompleteRoundedRectDrawable().apply {
            paint.color = context.getColorCompat(R.color.product_related_more_bg)
        }
    }

}