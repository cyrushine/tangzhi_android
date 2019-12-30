package com.ifanr.tangzhi.ui.productparam.widgets

import com.airbnb.epoxy.AutoModel
import com.ifanr.tangzhi.ui.base.model.BaseTypedController
import com.ifanr.tangzhi.ui.productparam.*
import com.ifanr.tangzhi.util.uuid

class Controller: BaseTypedController<Params>() {

    @AutoModel
    lateinit var highlightHeader: HighlightHeaderModel_

    @AutoModel
    lateinit var allHeader: AllHeaderModel_

    override fun buildModels(data: Params?) {
        if (data?.highlight?.isNotEmpty() == true) {
            add(highlightHeader)
            data.highlight.forEach {
                highlightItem {
                    id(uuid())
                    param(it)
                }
            }
        }

        if (data?.all?.isNotEmpty() == true) {
            add(allHeader)
            data.all.forEach {
                if (it.value.isNotEmpty()) {
                    allGroupHeader {
                        id(uuid())
                        content(it.key)
                    }

                    it.value.forEach {
                        item {
                            id(uuid())
                            param(it)
                        }
                    }
                }
            }
        }
    }

}