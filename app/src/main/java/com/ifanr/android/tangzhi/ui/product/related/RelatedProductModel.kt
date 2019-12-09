package com.ifanr.android.tangzhi.ui.product.related

import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.ifanr.android.tangzhi.R
import com.ifanr.android.tangzhi.epoxy.KotlinEpoxyHolder
import com.ifanr.android.tangzhi.ext.roundedRect
import com.ifanr.android.tangzhi.model.Product
import com.ifanr.android.tangzhi.ui.widgets.RatingBar
import com.ifanr.android.tangzhi.ui.widgets.ScoreTextView

@EpoxyModelClass(layout = R.layout.product_related)
abstract class RelatedProductModel: EpoxyModelWithHolder<RelatedProductModel.Holder>() {

    @EpoxyAttribute
    lateinit var product: Product

    override fun bind(holder: Holder) {
        Glide.with(holder.coverIV).roundedRect().load(product.coverImage).into(holder.coverIV)
        holder.nameTV.text = product.name
        holder.scoreTV.setScore(product.rating)
        holder.ratingbar.setProgress(product.rating)
    }

    class Holder: KotlinEpoxyHolder() {
        val coverIV by bind<ImageView>(R.id.coverIV)
        val nameTV by bind<TextView>(R.id.nameTv)
        val scoreTV by bind<ScoreTextView>(R.id.scoreTv)
        val ratingbar by bind<RatingBar>(R.id.ratingbar)
    }
}