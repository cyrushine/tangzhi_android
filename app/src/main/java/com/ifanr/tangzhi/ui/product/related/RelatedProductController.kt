package com.ifanr.tangzhi.ui.product.related

import com.airbnb.epoxy.AutoModel
import com.ifanr.tangzhi.Const
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.ui.base.model.BaseTyped3Controller

class RelatedProductController: BaseTyped3Controller<List<Product>, Int, String>() {

    @AutoModel
    lateinit var loading: LoadingModel

    @AutoModel
    lateinit var more: MoreViewModel_

    override fun buildModels(products: List<Product>?, total: Int?, productId: String?) {
        val totalCount = total ?: 0
        if (products?.isNotEmpty() == true) {
            products.take(RelatedProductList.MAX).forEach {
                relatedProduct {
                    id(it.id)
                    product(it)
                }
            }
            if (totalCount > RelatedProductList.MAX) {
                more.productId(productId ?: "")
                more.addTo(this)
            }

        } else {
            loading.addTo(this)
        }
    }

    fun setData(
        productId: String,
        products: List<Product>,
        total: Int
    ) {
        super.setData(products, total, productId)
    }
}