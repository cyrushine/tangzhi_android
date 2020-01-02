package com.ifanr.tangzhi.ui.base

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseItemDecoration: RecyclerView.ItemDecoration() {

    open fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        vh: RecyclerView.ViewHolder,
        vt: Int,
        position: Int
    ) {}

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(0, 0, 0, 0)
        val vh = parent.getChildViewHolder(view)
        val vt = vh.itemViewType
        val position = vh.adapterPosition
        getItemOffsets(outRect, view, parent, vh, vt, position)
    }
}