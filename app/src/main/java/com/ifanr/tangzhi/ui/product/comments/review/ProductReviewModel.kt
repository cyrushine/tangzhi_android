package com.ifanr.tangzhi.ui.product.comments.review

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.epoxy.KotlinEpoxyHolder
import com.ifanr.tangzhi.model.Comment
import com.ifanr.tangzhi.ui.widgets.EditorChoiceImageView
import com.ifanr.tangzhi.ui.widgets.RatingBar

@EpoxyModelClass(layout = R.layout.product_comment)
abstract class ProductReviewModel: EpoxyModelWithHolder<ProductReviewModel.Holder>() {

    @EpoxyAttribute
    lateinit var comment: Comment

    override fun bind(holder: Holder) {
        holder.content.text = comment.content
        holder.editorChoice.visibility = View.GONE
        holder.ratingBar.visibility = View.GONE
    }

    class Holder: KotlinEpoxyHolder() {
        val avatar by bind<ImageView>(R.id.avatar)
        val name by bind<TextView>(R.id.nameTv)
        val ratingBar by bind<RatingBar>(R.id.ratingbar)
        val editorChoice by bind<EditorChoiceImageView>(R.id.editorChoiceIv)
        val content by bind<TextView>(R.id.contentTv)
    }
}