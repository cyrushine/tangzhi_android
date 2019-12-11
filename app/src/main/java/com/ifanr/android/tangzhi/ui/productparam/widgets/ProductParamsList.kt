package com.ifanr.android.tangzhi.ui.productparam.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ifanr.android.tangzhi.R
import com.ifanr.android.tangzhi.ext.dp2px
import com.ifanr.android.tangzhi.ext.getColorCompat
import com.ifanr.android.tangzhi.ui.base.AppEpoxyRV

class ProductParamsList: AppEpoxyRV {

    companion object {
        private const val TAG = "ProductParamsList"
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        addItemDecoration(object: ItemDecoration() {

            private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = context.getColorCompat(R.color.product_param_border)
                style = Paint.Style.STROKE
                strokeWidth = context.dp2px(1).toFloat()
            }

            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: State
            ) {
                outRect.set(0, 0, 0, 0)
                val vh = parent.getChildViewHolder(view)
                val vt = vh.itemViewType
                val position = vh.adapterPosition
                val count = parent.adapter?.itemCount ?: 0

                when (vt) {
                    R.layout.product_param_all_header, R.layout.product_param_highlight_header -> {
                        outRect.bottom = context.dp2px(20)
                        outRect.top = context.dp2px(if (position == 0) 30 else 42)
                    }

                    R.layout.product_param_all_group_header -> {
                        outRect.bottom = context.dp2px(16)
                        val previous = parent.findViewHolderForAdapterPosition(position - 1)?.itemViewType
                        if (previous == R.layout.product_param_item)
                            outRect.top = context.dp2px(40)
                    }

                    R.layout.product_param_highlight -> {
                        outRect.bottom = context.dp2px(8)
                    }

                    R.layout.product_param_item -> {
                        val next = parent.findViewHolderForAdapterPosition(position + 1)?.itemViewType
                        if (position == count - 1 || next == R.layout.product_param_all_group_header)
                            outRect.bottom = context.dp2px(40)
                    }
                }
            }

            override fun onDrawOver(c: Canvas, parent: RecyclerView, state: State) {
                parent.children.forEach {
                    val vh = parent.getChildViewHolder(it)
                    val v = vh.itemView
                    if (vh.itemViewType == R.layout.product_param_item) {
                        c.drawRect(v.left.toFloat(), v.top.toFloat(), v.right.toFloat(), v.bottom.toFloat(), paint)
                    }
                }
            }
        })
    }


}