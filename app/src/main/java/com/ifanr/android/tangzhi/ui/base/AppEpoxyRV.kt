package com.ifanr.android.tangzhi.ui.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.airbnb.epoxy.EpoxyRecyclerView

open class AppEpoxyRV: EpoxyRecyclerView {

    constructor(context: Context) : super(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        overScrollMode = View.OVER_SCROLL_NEVER
    }
}