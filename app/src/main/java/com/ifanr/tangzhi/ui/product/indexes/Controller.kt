package com.ifanr.tangzhi.ui.product.indexes

import com.airbnb.epoxy.AutoModel
import com.ifanr.tangzhi.ui.base.model.BaseTypedController
import com.ifanr.tangzhi.util.uuid

class Controller: BaseTypedController<ProductRating>() {

    @AutoModel
    lateinit var header: HeaderModel_

    @AutoModel
    lateinit var userGroupHeader: UserGroupHeaderModel_

    @AutoModel
    lateinit var orgGroupHeader: OrgGroupHeaderModel_

    @AutoModel
    lateinit var thirdPartyGroupHeader: ThirdPartyGroupHeaderModel_

    override fun buildModels(data: ProductRating?) {
        with(header) {
            name(data?.name ?: "")
            score(data?.rating ?: 0f)
            addTo(this@Controller)
        }

        data?.usersRating?.takeIf { it.rating > 0 && it.count > 0 }?.also {
            add(userGroupHeader)
            item {
                id(uuid())
                data(ProductRating.RatingRecord(name = "${it.count}人参与评分", rating = it.rating))
            }
        }

        data?.orgRating?.takeIf { it.isNotEmpty() }?.also {
            add(orgGroupHeader)
            it.forEach {
                item {
                    id(uuid())
                    data(it)
                }
            }
        }

        data?.thirdPartyRating?.takeIf { it.isNotEmpty() }?.also {
            add(thirdPartyGroupHeader)
            it.forEach {
                item {
                    id(uuid())
                    data(it)
                }
            }
        }
    }
}