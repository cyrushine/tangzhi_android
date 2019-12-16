package com.ifanr.tangzhi.ui.product.related

import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.Routes
import com.ifanr.tangzhi.epoxy.KotlinEpoxyHolder
import com.ifanr.tangzhi.ext.roundedRect
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.ui.widgets.RatingBar
import com.ifanr.tangzhi.ui.widgets.ScoreTextView

@EpoxyModelClass(layout = R.layout.product_related)
abstract class RelatedProductModel: EpoxyModelWithHolder<RelatedProductModel.Holder>() {

    @EpoxyAttribute
    lateinit var product: Product

    override fun bind(holder: Holder) {
        Glide.with(holder.coverIV).roundedRect().load(product.coverImage).into(holder.coverIV)
        holder.nameTV.text = product.name
        holder.scoreTV.setScore(product.rating)
        holder.ratingbar.setProgress(product.rating)
        holder.view.setOnClickListener {
            if (this::product.isInitialized) {
                ARouter.getInstance().build(Routes.product)
                    .withString(Routes.productId, product.id)
                    .navigation(holder.ctx)
            }
        }
    }

    class Holder: KotlinEpoxyHolder() {
        val coverIV by bind<ImageView>(R.id.coverIV)
        val nameTV by bind<TextView>(R.id.nameTv)
        val scoreTV by bind<ScoreTextView>(R.id.scoreTv)
        val ratingbar by bind<RatingBar>(R.id.ratingbar)
    }
}