package com.ifanr.android.tangzhi.ui.base

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyRecyclerView

/**
 * 把 [EpoxyRecyclerView] 当作水平的 LinearLayout 使用
 * 这里做一些优化
 */
open class FlatHorizontalEpoxyRV: EpoxyRecyclerView {

    constructor(context: Context) : super(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        isNestedScrollingEnabled = false
    }

    override fun createLayoutManager(): LayoutManager {
        return HorizontalLayoutManager(context)
    }

    override fun canScrollHorizontally(direction: Int): Boolean {
        return false
    }

    override fun canScrollVertically(direction: Int): Boolean {
        return false
    }

    class HorizontalLayoutManager: LinearLayoutManager {

        constructor(context: Context?) : super(context)
        constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
            context,
            orientation,
            reverseLayout
        )

        constructor(
            context: Context?,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int
        ) : super(context, attrs, defStyleAttr, defStyleRes)

        init {
            orientation = RecyclerView.HORIZONTAL
        }

        override fun canScrollVertically(): Boolean {
            return false
        }

        override fun canScrollHorizontally(): Boolean {
            return true
        }
    }
}