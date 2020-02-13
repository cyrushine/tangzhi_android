package com.ifanr.tangzhi.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.ifanr.tangzhi.ui.getStatusBarHeight

class AppStatusBar: View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(context.getStatusBarHeight(), MeasureSpec.EXACTLY))
    }
}