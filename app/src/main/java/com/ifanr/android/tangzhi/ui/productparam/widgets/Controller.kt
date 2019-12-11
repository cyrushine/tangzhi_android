package com.ifanr.android.tangzhi.ui.productparam.widgets

import com.airbnb.epoxy.AutoModel
import com.ifanr.android.tangzhi.ui.base.BaseEpoxyController
import com.ifanr.android.tangzhi.ui.productparam.*
import com.ifanr.android.tangzhi.util.uuid

class Controller: BaseEpoxyController() {

    var params = Params(
        highlight = emptyList(),
        all = emptyMap()
    )
        set(value) {
            field = value
            requestModelBuild()
        }

    @AutoModel
    lateinit var highlightHeader: HighlightHeaderModel_

    @AutoModel
    lateinit var allHeader: AllHeaderModel_

    override fun buildModels() {
        if (params.highlight.isNotEmpty()) {
            add(highlightHeader)
            params.highlight.forEach {
                HighlightItemModel_()
                    .id(uuid())
                    .param(it)
                    .addTo(this)
            }
        }

        if (params.all.isNotEmpty()) {
            add(allHeader)
            params.all.forEach {
                if (it.value.isNotEmpty()) {
                    AllGroupHeaderModel_()
                        .id(uuid())
                        .content(it.key)
                        .addTo(this)

                    it.value.forEach {
                        ItemModel_()
                            .id(uuid())
                            .param(it)
                            .addTo(this)
                    }
                }
            }
        }
    }

}