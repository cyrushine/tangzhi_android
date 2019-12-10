package com.ifanr.android.tangzhi.ui.product.list

import com.airbnb.epoxy.AutoModel
import com.ifanr.android.tangzhi.R
import com.ifanr.android.tangzhi.model.ProductList
import com.ifanr.android.tangzhi.ui.base.BaseEpoxyController
import com.ifanr.android.tangzhi.ui.product.widgets.SectionHeaderViewModel_
import com.minapp.android.sdk.util.PagedList

class ProductListController: BaseEpoxyController() {

    private var productLists: PagedList<ProductList>? = null

    @AutoModel
    lateinit var header: SectionHeaderViewModel_

    override fun buildModels() {
        val total = productLists?.totalCount ?: 0L
        val list = productLists?.objects ?: emptyList()

        if (list.isNotEmpty() && total > 0) {
            with(header) {
                title(R.string.product_list_title)
                count(R.string.product_list_size, total)
                addTo(this@ProductListController)
            }

            list.map { ProductListModel_().data(it).id(it.id) }.also { add(it) }
        }
    }

    fun setProductLists(list: PagedList<ProductList>?) {
        productLists = list
        requestModelBuild()
    }

}