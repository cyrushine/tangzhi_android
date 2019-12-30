package com.ifanr.tangzhi.ui.relatedproducts

import com.airbnb.epoxy.AutoModel
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.ui.base.model.BaseTypedController
import com.ifanr.tangzhi.ui.base.model.ListEndSlogonModel_
import com.ifanr.tangzhi.ui.base.model.ListHeaderCountModel_

class Controller: BaseTypedController<List<Product>>() {

    @AutoModel
    lateinit var header: ListHeaderCountModel_

    @AutoModel
    lateinit var end: ListEndSlogonModel_

    override fun buildModels(data: List<Product>) {
        header.stringRes(R.string.related_products_count)
        header.count(data.size)
        add(header)

        if (data.isNotEmpty()) {
            data.forEach {
                relatedProductItem {
                    id(it.id)
                    product(it)
                }
            }
            add(end)
        }
    }

}