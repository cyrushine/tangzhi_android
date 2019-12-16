package com.ifanr.tangzhi.ui.product.list

import com.airbnb.epoxy.AutoModel
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.model.ProductList
import com.ifanr.tangzhi.ui.base.BaseEpoxyController
import com.ifanr.tangzhi.ui.base.BaseTypedController
import com.ifanr.tangzhi.ui.product.widgets.SectionHeaderViewModel_
import com.minapp.android.sdk.util.PagedList

class ProductListController: BaseTypedController<PagedList<ProductList>>() {

    @AutoModel
    lateinit var header: SectionHeaderViewModel_

    override fun buildModels(data: PagedList<ProductList>?) {
        val total = data?.totalCount ?: 0L
        val list = data?.objects ?: emptyList()

        if (list.isNotEmpty() && total > 0) {
            with(header) {
                title(R.string.product_list_title)
                count(R.string.product_list_size, total)
                addTo(this@ProductListController)
            }

            list.forEach {
                productList {
                    id(it.id)
                    data(it)
                }
            }
        }
    }

}