package com.ifanr.tangzhi.ui.product.comments.review.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyViewHolder
import com.ifanr.tangzhi.Const
import com.ifanr.tangzhi.model.Comment
import com.ifanr.tangzhi.ui.base.model.BaseTypedController
import com.ifanr.tangzhi.ui.base.model.LoadMoreAwaredController
import com.ifanr.tangzhi.ui.base.widget.AppEpoxyRV
import com.ifanr.tangzhi.ui.product.comments.review.productReview

/**
 * 点评列表
 */
class ReviewList: AppEpoxyRV {

    interface Listener {
        fun onReplyClick(position: Int) {}
        fun onClick(position: Int) {}
    }

    private val controller =
        ProductReviewController()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        setRecycledViewPool(RecycledViewPool())
        setController(controller)
    }

    fun setData(data: List<Comment>, scrollTop: Boolean = false) {
        controller.setData(data)
        if (scrollTop)
            scrollToPosition(0)
    }

    fun setLoadMoreListener(l: () -> Unit) {
        controller.loadMoreListener = l
    }

    fun setListener(l: Listener) {
        controller.listener = l
    }
}

class ProductReviewController: LoadMoreAwaredController<List<Comment>>() {

    var listener: ReviewList.Listener = object: ReviewList.Listener {}

    override fun buildModels(data: List<Comment>?) {
        data?.forEach {
            productReview {
                id(it.id)
                comment(it)
                onReplyClick { _, _, _, position ->
                    listener.onReplyClick(position)
                }
                onClick { _, _, _, position ->
                    listener.onClick(position)
                }
            }
        }
    }
}