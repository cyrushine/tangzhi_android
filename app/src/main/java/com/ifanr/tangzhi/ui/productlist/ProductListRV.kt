package com.ifanr.tangzhi.ui.productlist

import android.content.Context
import android.util.AttributeSet
import androidx.paging.PagedList
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.model.ProductList
import com.ifanr.tangzhi.ui.base.widget.AppEpoxyRV
import com.ifanr.tangzhi.ui.product.list.ProductListItemDecoration

class ProductListRV: AppEpoxyRV {

    private val controller = ProductListController()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setController(controller)
        addItemDecoration(ProductListItemDecoration(
            dividerColor = R.color.product_list_divider,
            ctx = context
        ))
    }

    fun submitList(list: PagedList<ProductList>) {
        controller.submitList(list)
    }
}