package com.ifanr.tangzhi.ui.follows

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyModel
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.ui.base.model.BasePagedListController
import com.ifanr.tangzhi.ui.base.widget.AppEpoxyRV
import com.ifanr.tangzhi.ui.relatedproducts.RelatedProductItemModel_

class FollowsList: AppEpoxyRV {

    private val ctl = FollowsController()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setController(ctl)
        addItemDecoration(Decoration(context))
    }

    fun submitList(list: PagedList<Product>) {
        ctl.submitPagedList(list)
    }
}

class FollowsController: BasePagedListController<Product>() {
    override fun buildItemModel(currentPosition: Int, item: Product?): EpoxyModel<*> {
        val data = item ?: Product()
        val model = RelatedProductItemModel_().apply {
            id(data.id)
            product(data)
        }
        return model
    }
}

class Decoration(
    private val ctx: Context
): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.set(0, 0, 0, 0)
        val vh = parent.getChildViewHolder(view)
        val vt = vh.itemViewType
        val position = vh.adapterPosition
        val count = parent.adapter?.itemCount ?: 0
        val padding = ctx.dp2px(20)

        if (vt == R.layout.related_products_item) {
            outRect.left = padding
            outRect.right = padding
            outRect.bottom = padding
            if (position == 0)
                outRect.top = padding
        }
    }
}