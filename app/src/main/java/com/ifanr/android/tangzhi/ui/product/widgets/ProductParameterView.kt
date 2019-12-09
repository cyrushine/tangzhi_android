package com.ifanr.android.tangzhi.ui.product.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.ifanr.android.tangzhi.R
import com.ifanr.android.tangzhi.ui.icons.IconView

/**
 * 产品参数卡片
 */
class ProductParameterView: ConstraintLayout {

    constructor(context: Context?) : super(context) { init() }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init() }

    private fun init() {
        setBackgroundResource(R.drawable.product_parameter_view_bg)
        LayoutInflater.from(context).inflate(R.layout.product_parameter_view, this, true)
    }
}

/**
 * 文本：产品参数
 */
class ProductParameterText: IconView {

    companion object {
        private const val TEXT_SIZE = 12f
    }

    constructor(context: Context?) : super(context) { init() }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init() }

    @SuppressLint("SetTextI18n")
    private fun init() {
        setTextSize(TypedValue.COMPLEX_UNIT_SP,
            TEXT_SIZE
        )
        setTextColor(Color.WHITE)
        text = "${context.getString(R.string.ic_spec_icon_detail)}  ${context.getString(
            R.string.product_param
        )}"
    }

}