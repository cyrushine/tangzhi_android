package com.ifanr.tangzhi.ui.comment.widget

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
import com.airbnb.epoxy.AutoModel
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.ext.getColorCompat
import com.ifanr.tangzhi.model.Comment
import com.ifanr.tangzhi.ui.base.model.BaseEpoxyController
import com.ifanr.tangzhi.ui.base.model.BaseTypedController
import com.ifanr.tangzhi.ui.base.widget.AppEpoxyRV
import com.ifanr.tangzhi.ui.product.comments.review.productReview

class CommentList: AppEpoxyRV {

    interface Listener {
        fun onReplyClick(position: Int) {}
        fun onUpClick(position: Int) {}
        fun onOptionClick(position: Int) {}
    }

    private val controller: Controller

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        controller = Controller()
        setController(controller)
        addItemDecoration(ItemDecorationImpl(context))
    }

    fun setData(data: List<Comment>) {
        controller.setData(data)
    }

    fun setListener(l: Listener) {
        controller.listener = l
    }
}

class Controller: BaseTypedController<List<Comment>>() {

    var listener: CommentList.Listener = object: CommentList.Listener {}

    @AutoModel
    lateinit var reviewTitle: CommentReviewTitleModel_

    @AutoModel
    lateinit var allTitle: CommentAllTitleModel_

    override fun buildModels(data: List<Comment>?) {
        if (!data.isNullOrEmpty()) {
            add(reviewTitle)

            val review = data.first()
            productReview {
                id(review.id)
                onClick { _ -> }
                onReplyClick { _, _, _, position ->
                    listener.onReplyClick(position)
                }
                comment(review)
                dateVisiable(true)
                optionVisiable(true)
                contentExpanded(true)
            }

            add(allTitle)
            data.takeLast(data.size - 1).forEach {
                commentItem {
                    id(it.id)
                    data(it)
                    onReplyClick { _, _, _, position ->
                        listener.onReplyClick(position)
                    }
                    onUpClick { _, _, _, position ->
                        listener.onUpClick(position)
                    }
                    onOptionClick { _, _, _, position ->
                        listener.onOptionClick(position)
                    }
                }
            }
        }
    }
}

class ItemDecorationImpl (ctx: Context): RecyclerView.ItemDecoration() {

    private val dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeWidth = ctx.dp2px(1f)
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
        val ctx = parent.context

        when (position) {
            0 -> {
                outRect.left = ctx.dp2px(18)
                outRect.top = ctx.dp2px(20)
            }
            2 -> {
                outRect.bottom = ctx.dp2px(25)
            }
        }

        if (position > 3 && vt == R.layout.comment_item) {
            outRect.top = ctx.dp2px(25)
        }

        val nextVt = runCatching { parent.adapter?.getItemViewType(position + 1) ?: 0 }
            .getOrDefault(0)
        if (position > 2 && nextVt == R.layout.comment_item) {
            outRect.bottom = ctx.dp2px(7)
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        parent.children.forEach { child ->
            val vh = parent.getChildViewHolder(child)
            val position = vh.adapterPosition
            val vt = vh.itemViewType
            val ctx = parent.context

            if (position > 3 && vt == R.layout.comment_item) {
                val top = child.top - ctx.dp2px(25f)
                c.drawLine(0f, top, parent.measuredWidth.toFloat(), top, dividerPaint)
            }
        }
    }
}