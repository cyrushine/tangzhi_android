package com.ifanr.tangzhi.ui.productlist

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.EpoxyModel
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.model.ProductList
import com.ifanr.tangzhi.ui.base.*
import com.ifanr.tangzhi.ui.product.list.ProductListModel
import com.ifanr.tangzhi.ui.product.list.ProductListModel_
import com.ifanr.tangzhi.ui.product.list.productList

class ProductListController: BasePagedListController<ProductList>() {

    @AutoModel
    lateinit var header: ListHeaderCountModel_

    @AutoModel
    lateinit var end: ListEndSlogonModel_

    override fun buildItemModel(currentPosition: Int, item: ProductList?): EpoxyModel<*> {
        return if (item != null)
            ProductListModel_().apply {
                id(item.id)
                data(item)
                style(ProductListModel.Style.PRODUCT_LIST)
            }
        else
            BlankModel_()
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        header.stringRes(R.string.product_list_page_count)
        header.count(models.size)
        add(header)

        if (models.isNotEmpty()) {
            add(models)
            add(end)
        }
    }
}
