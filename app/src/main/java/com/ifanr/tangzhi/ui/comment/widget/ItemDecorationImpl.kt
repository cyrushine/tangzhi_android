package com.ifanr.tangzhi.ui.comment.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.ext.getColorCompat

class ItemDecorationImpl (ctx: Context): RecyclerView.ItemDecoration() {

    companion object {
        private val VT_CHILDREN = arrayOf(
            R.layout.comment_child, R.layout.comment_child_fold,
            R.layout.comment_child_load_more)
    }

    private val dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        .apply {
        style = Paint.Style.FILL
        strokeWidth = ctx.dp2px(0.5f)
        color = ctx.getColorCompat(R.color.base_e7)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(0, 0, 0, 0)
        val vh = parent.getChildViewHolder(view)
        val position = vh.adapterPosition
        val vt = vh.itemViewType
        val nextVt = runCatching { parent.adapter?.getItemViewType(position + 1) ?: 0 }
            .getOrDefault(0)
        val ctx = parent.context

        // 「评论详情」和「全部回复」
        when (position) {
            0 -> {
                outRect.left = ctx.dp2px(18)
                outRect.top = ctx.dp2px(20)
            }
            2 -> {
                outRect.bottom = ctx.dp2px(25)
            }
        }

        // 一级评论
        if (position > 3 && vt == R.layout.comment_item) {
            outRect.top = ctx.dp2px(25)
        }
        if (position > 2 && nextVt == R.layout.comment_item) {
            outRect.bottom = ctx.dp2px(7)
        }

        // 二级评论
        if (vt in VT_CHILDREN) {
            outRect.left = ctx.dp2px(55)
            outRect.right = ctx.dp2px(20)
            if (nextVt == R.layout.comment_item) {
                outRect.bottom = ctx.dp2px(25)
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        parent.children.forEach { child ->
            val vh = parent.getChildViewHolder(child)
            val position = vh.adapterPosition
            val vt = vh.itemViewType
            val ctx = parent.context
            val previousVT = runCatching { parent.adapter?.getItemViewType(position - 1) ?: 0 }
                .getOrDefault(0)

            // 一级评论之间的分割线
            if (position > 3 && vt == R.layout.comment_item) {
                val top = child.top - ctx.dp2px(25f)
                c.drawLine(0f, top, parent.measuredWidth.toFloat(), top, dividerPaint)
            }

            // 二级评论之间的分割线
            if (vt == R.layout.comment_child && previousVT == R.layout.comment_child) {
                c.drawLine(
                    child.left + ctx.dp2px(16f),
                    child.top.toFloat(),
                    child.right - ctx.dp2px(16f),
                    child.top.toFloat(),
                    dividerPaint)
            }
        }
    }
}