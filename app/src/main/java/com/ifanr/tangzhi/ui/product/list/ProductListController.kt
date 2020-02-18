package com.ifanr.tangzhi.ui.product.list

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.OnModelClickListener
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.model.Page
import com.ifanr.tangzhi.model.ProductList
import com.ifanr.tangzhi.ui.base.model.BaseTypedController
import com.ifanr.tangzhi.ui.product.widgets.SectionHeaderView
import com.ifanr.tangzhi.ui.product.widgets.SectionHeaderViewModel_

class ProductListController (
    private val onHeaderClickListener: OnModelClickListener<SectionHeaderViewModel_, SectionHeaderView>
): BaseTypedController<Page<ProductList>>() {

    companion object {
        private const val TAG = "ProductListController"
        private const val MAX = 5
    }

    @AutoModel
    lateinit var header: SectionHeaderViewModel_

    private val emptyHeaderClick: OnModelClickListener<SectionHeaderViewModel_, SectionHeaderView> =
        OnModelClickListener { _, _, _, _ ->  }

    override fun buildModels(data: Page<ProductList>?) {
        val total = data?.total ?: 0
        val list = data?.data ?: emptyList()

        if (list.isNotEmpty() && total > 0) {
            with(header) {
                title(R.string.product_list_title)
                count(R.string.product_list_size, total)
                countVisible(total > MAX)
                listener(if (total > MAX) onHeaderClickListener else emptyHeaderClick)
                addTo(this@ProductListController)
            }

            list.take(MAX).forEach {
                productList {
                    id(it.id)
                    data(it)
                    style(ProductListModel.Style.PRODUCT_PAGE)
                }
            }
        }
    }

}