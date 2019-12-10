package com.ifanr.android.tangzhi.ui.product.list

import android.content.Context
import android.util.AttributeSet
import com.ifanr.android.tangzhi.model.ProductList
import com.ifanr.android.tangzhi.ui.base.FlatVerticalEpoxyRV
import com.minapp.android.sdk.util.PagedList

class ProductLists: FlatVerticalEpoxyRV {

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
    }

    fun setProductLists(list: PagedList<ProductList>?) {
        controller.setProductLists(list)
    }
}