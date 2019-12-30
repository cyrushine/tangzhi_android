package com.ifanr.tangzhi.ui.productlist

import android.util.Log
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.EpoxyModel
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.model.ProductList
import com.ifanr.tangzhi.ui.base.model.BasePagedListController
import com.ifanr.tangzhi.ui.base.model.BlankModel_
import com.ifanr.tangzhi.ui.base.model.ListEndSlogonModel_
import com.ifanr.tangzhi.ui.base.model.ListHeaderCountModel_
import com.ifanr.tangzhi.ui.product.list.ProductListModel
import com.ifanr.tangzhi.ui.product.list.ProductListModel_

class ProductListController: BasePagedListController<ProductList>() {

    companion object {
        private const val TAG = "ProductListController"
    }

    @AutoModel
    lateinit var header: ListHeaderCountModel_

    @AutoModel
    lateinit var end: ListEndSlogonModel_

    override fun buildItemModel(currentPosition: Int, item: ProductList?): EpoxyModel<*> {
        Log.d(TAG, "${currentPosition}, ${item?.name}")
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
