package com.ifanr.tangzhi.ui.product.comments.review

import com.ifanr.tangzhi.model.Comment
import com.ifanr.tangzhi.ui.base.BaseTypedController

class ProductReviewController: BaseTypedController<List<Comment>>() {

    override fun buildModels(data: List<Comment>?) {
        data?.forEach {
            productReview {
                id(it.id)
                comment(it)
            }
        }
    }

}