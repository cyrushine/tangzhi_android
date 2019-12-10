package com.ifanr.android.tangzhi.ui.product.related

import com.airbnb.epoxy.AutoModel
import com.ifanr.android.tangzhi.Const
import com.ifanr.android.tangzhi.model.Product
import com.ifanr.android.tangzhi.ui.base.BaseEpoxyController
import com.ifanr.android.tangzhi.ui.product.RelatedProducts

class RelatedProductController: BaseEpoxyController() {

    private var products: List<Product> = emptyList()
    private var total = 0

    @AutoModel
    lateinit var loading: LoadingModel

    @AutoModel
    lateinit var more: MoreViewModel_

    override fun buildModels() {
        if (products.isNotEmpty()) {
            products.forEach {
                RelatedProductModel_()
                    .id(it.id)
                    .product(it)
                    .addTo(this)
            }
            if (products.size == Const.PRODUCT_RELATED_MAX && total >= Const.PRODUCT_RELATED_MAX) {
                more.addTo(this)
            }

        } else {
            loading.addTo(this)
        }
    }

    fun setProducts(data: RelatedProducts) {
        products = data.products
        total = data.total
        requestModelBuild()
    }

}