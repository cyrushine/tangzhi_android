package com.ifanr.tangzhi.ui.base.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager

open class FlatGridEpoxyRV: AppEpoxyRV {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        isNestedScrollingEnabled = false

    }

    override fun canScrollHorizontally(direction: Int): Boolean {
        return false
    }

    override fun canScrollVertically(direction: Int): Boolean {
        return false
    }

    class FlatGridLayoutManager: GridLayoutManager {

        constructor(
            context: Context?,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int
        ) : super(context, attrs, defStyleAttr, defStyleRes)

        constructor(context: Context?, spanCount: Int) : super(context, spanCount)
        constructor(
            context: Context?,
            spanCount: Int,
            orientation: Int,
            reverseLayout: Boolean
        ) : super(context, spanCount, orientation, reverseLayout)

        override fun canScrollVertically(): Boolean {
            return false
        }

        override fun canScrollHorizontally(): Boolean {
            return true
        }
    }
}
