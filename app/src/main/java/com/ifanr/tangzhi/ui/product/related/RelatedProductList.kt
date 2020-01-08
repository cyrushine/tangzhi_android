package com.ifanr.tangzhi.ui.product.related

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyRecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.ui.product.RelatedProducts
import com.ifanr.tangzhi.ui.product.widgets.SectionHeaderView

class RelatedProductList: ConstraintLayout {

    private val header: SectionHeaderView
    private val list: EpoxyRecyclerView
    private val controller = RelatedProductController()
    private var productId: String = ""

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        LayoutInflater.from(context).inflate(R.layout.related_product_list, this, true)
        header = findViewById(R.id.header)
        list = findViewById(R.id.list)
        list.setController(controller)
        list.addItemDecoration(Decoration())
        header.hotspot.setOnClickListener {
            ARouter.getInstance().build(Routes.relatedProducts)
                .withString(Routes.relatedProductId, productId)
                .navigation(context)
        }
    }

    fun setData(data: RelatedProducts) {
        if (data.total <= 0) {
            visibility = View.GONE
        } else {
            visibility = View.VISIBLE
            header.setTitle(context.getString(R.string.product_related_product))
            header.setCount(context.getString(R.string.product_related_product_total, data.total))
            controller.setData(data.productId, data.products, data.total)
            productId = data.productId
        }
    }
}

class Decoration: RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val ctx = parent.context
        outRect.set(ctx.dp2px(15), 0, 0, 0)

        val position = parent.findContainingViewHolder(view)?.adapterPosition
        val count = parent.adapter?.itemCount
        if (position != null && count != null && count > 1) {
            when (position) {
                0 -> {
                    outRect.left = ctx.dp2px(20)
                }
                count - 1 -> {
                    outRect.right = ctx.dp2px(15)
                }
            }
        }
    }
}