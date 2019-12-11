package com.ifanr.android.tangzhi.ui.product.indexes

import com.airbnb.epoxy.AutoModel
import com.ifanr.android.tangzhi.model.Product
import com.ifanr.android.tangzhi.ui.base.BaseEpoxyController
import com.ifanr.android.tangzhi.util.uuid
import java.util.*

class Controller: BaseEpoxyController() {

    @AutoModel
    lateinit var header: HeaderModel_

    @AutoModel
    lateinit var userGroupHeader: UserGroupHeaderModel_

    @AutoModel
    lateinit var orgGroupHeader: OrgGroupHeaderModel_

    @AutoModel
    lateinit var thirdPartyGroupHeader: ThirdPartyGroupHeaderModel_

    var data = ProductRating(Product())
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        with(header) {
            name(data.name)
            score(data.rating)
            addTo(this@Controller)
        }

        data.usersRating.takeIf { it.rating > 0 && it.count > 0 }?.also {
            add(userGroupHeader)
            ItemModel_()
                .id(uuid())
                .data(
                    ProductRating.RatingRecord(
                        name = "${it.count}人参与评分",
                        rating = it.rating
                ))
                .addTo(this)
        }

        data.orgRating.takeIf { it.isNotEmpty() }?.also {
            add(orgGroupHeader)
            it.forEach {
                ItemModel_().id(uuid()).data(it).addTo(this)
            }
        }

        data.thirdPartyRating.takeIf { it.isNotEmpty() }?.also {
            add(thirdPartyGroupHeader)
            it.forEach {
                ItemModel_().id(uuid()).data(it).addTo(this)
            }
        }
    }
}