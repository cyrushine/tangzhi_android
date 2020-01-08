package com.ifanr.tangzhi.ui.product.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.ifanr.tangzhi.ui.base.widget.AppScrollView

class ProductPanel: AppScrollView {

    var freeze = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return !freeze && super.dispatchTouchEvent(ev)
    }
}