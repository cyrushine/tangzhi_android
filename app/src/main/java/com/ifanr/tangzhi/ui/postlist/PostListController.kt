package com.ifanr.tangzhi.ui.postlist

import com.airbnb.epoxy.AutoModel
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.ui.base.BaseEpoxyController
import com.ifanr.tangzhi.ui.base.BaseTypedController
import com.ifanr.tangzhi.ui.base.ListEndSlogonModel_
import com.ifanr.tangzhi.ui.base.ListHeaderCountModel_
import kotlin.properties.Delegates

class PostListController: BaseTypedController<List<Product.CachedPost>>() {

    @AutoModel
    lateinit var header: ListHeaderCountModel_

    @AutoModel
    lateinit var end: ListEndSlogonModel_

    override fun buildModels(data: List<Product.CachedPost>) {
        header.stringRes(R.string.post_list_count)
        header.count(data.size)
        add(header)

        if (data.isNotEmpty()) {
            data.forEach {
                postListItem {
                    id(it.id)
                    post(it)
                }
            }
            add(end)
        }
    }


}