package com.ifanr.tangzhi.ui.postlist

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.ext.getColorCompat
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.ui.base.widget.AppEpoxyRV

class PostListRV: AppEpoxyRV {

    companion object {
        private const val ITEM_PADDING_BOTTOM = 22
    }

    private val controller = PostListController()

    private val itemDecoration = object: ItemDecoration() {

        private val paint: Paint by lazy { Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = context.getColorCompat(R.color.base_e7)
            style = Paint.Style.STROKE
            strokeWidth = context.dp2px(0.5f)
        } }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
            outRect.set(0, 0, 0, 0)
            val vh = parent.getChildViewHolder(view)
            val count = parent.adapter?.itemCount ?: 0
            val position = vh.adapterPosition

            if (vh.itemViewType == R.layout.postlist_item && position < count - 2) {
                outRect.bottom = context.dp2px(ITEM_PADDING_BOTTOM)
            }
        }

        override fun onDraw(c: Canvas, parent: RecyclerView, state: State) {
            parent.children.forEach { view ->
                val vh = parent.getChildViewHolder(view)
                val count = parent.adapter?.itemCount ?: 0
                val position = vh.adapterPosition

                if (vh.itemViewType == R.layout.postlist_item && position < count - 2) {
                    val y = view.bottom + context.dp2px(ITEM_PADDING_BOTTOM).div(2)
                    c.drawLine(view.left.toFloat(), y.toFloat(), view.right.toFloat(), y.toFloat(), paint)
                }
            }
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setController(controller)
        addItemDecoration(itemDecoration)
    }

    fun setData(data: List<Product.CachedPost>) {
        controller.setData(data)
    }
}