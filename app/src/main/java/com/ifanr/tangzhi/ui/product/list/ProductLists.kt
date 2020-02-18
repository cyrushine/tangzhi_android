package com.ifanr.tangzhi.ui.product.list

import android.content.Context
import android.util.AttributeSet
import com.airbnb.epoxy.OnModelClickListener
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.model.Page
import com.ifanr.tangzhi.model.ProductList
import com.ifanr.tangzhi.ui.base.widget.FlatVerticalEpoxyRV

/**
 * 糖纸清单
 */
class ProductLists: FlatVerticalEpoxyRV {

    private val controller = ProductListController(OnModelClickListener { _, _, _, _ ->
        ARouter.getInstance().build(Routes.productList)
            .withString(Routes.productListProductId, productId)
            .navigation(context)
    })

    private var productId: String = ""

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
            R.color.product_product_list_divider, context, firstPaddingTop = 12, lastPaddingBottom = 40))
    }

    fun setProductLists(productId: String, list: Page<ProductList>?) {
        this.productId = productId
        controller.setData(list)
    }
}