package com.ifanr.tangzhi.ui.product.list

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.ext.getColorCompat

class ProductListItemDecoration (
    @ColorRes dividerColor: Int,
    ctx: Context,
    @Dimension(unit = Dimension.DP) firstPaddingTop: Int = 0,
    @Dimension(unit = Dimension.DP) lastPaddingBottom: Int = 0
): RecyclerView.ItemDecoration() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = ctx.getColorCompat(dividerColor)
        strokeWidth = ctx.dp2px(1f)
    }

    private val paddingBottom = ctx.dp2px(40)
    private val firstPaddingTop = ctx.dp2px(firstPaddingTop)
    private val lastPaddingBottom = ctx.dp2px(lastPaddingBottom)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        parent.children.forEach { child ->
            match(parent, child) { _, last ->
                if (!last) {
                    val l = child.left.toFloat()
                    val r = child.right.toFloat()
                    val y = child.bottom + paddingBottom.div(2f)
                    c.drawLine(l, y, r, y, paint)
                }
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(0, 0, 0, 0)
        match(parent, view) { first, last ->
            if (!last) {
                outRect.bottom = paddingBottom
            }
            if (first)
                outRect.top += firstPaddingTop
            if (last)
                outRect.bottom += lastPaddingBottom
        }
    }

    private fun match(parent: RecyclerView, child: View, block: (first: Boolean, last: Boolean) -> Unit) {
        val vh = parent.getChildViewHolder(child)
        val viewType = vh.itemViewType
        val position = vh.adapterPosition
        val count = parent.adapter?.itemCount ?: 0
        val nextViewType = if (position + 1 < count)
            parent.adapter?.getItemViewType(position + 1) ?: 0
        else 0
        val previousViewType = if (position > 0)
            parent.adapter?.getItemViewType(position - 1) ?: 0
        else 0

        if (viewType == R.layout.product_list_item) {
            block.invoke(previousViewType != R.layout.product_list_item,
                nextViewType != R.layout.product_list_item)
        }
    }
}