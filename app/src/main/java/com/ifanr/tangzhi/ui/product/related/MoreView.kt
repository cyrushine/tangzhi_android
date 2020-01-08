package com.ifanr.tangzhi.ui.product.related

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.ext.getColorCompat
import com.ifanr.tangzhi.ui.widgets.CompleteRoundedRectDrawable

/**
 * 查看更多
 */
@ModelView(autoLayout = ModelView.Size.WRAP_WIDTH_WRAP_HEIGHT)
class MoreView: AppCompatTextView {

    @ModelProp
    @JvmField
    var productId: String = ""

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
        setOnClickListener {
            ARouter.getInstance().build(Routes.relatedProducts)
                .withString(Routes.relatedProductId, productId)
                .navigation(context)
        }
    }

}