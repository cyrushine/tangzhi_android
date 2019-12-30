package com.ifanr.tangzhi.ui.comment.widget

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.comment_review_title)
abstract class CommentReviewTitleModel: EpoxyModelWithHolder<CommentReviewTitleModel.Holder>() {

    class Holder: KotlinEpoxyHolder()
}