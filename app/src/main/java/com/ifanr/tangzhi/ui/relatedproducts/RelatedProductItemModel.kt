package com.ifanr.tangzhi.ui.relatedproducts

import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.Routes
import com.ifanr.tangzhi.epoxy.KotlinEpoxyHolder
import com.ifanr.tangzhi.ext.roundedRect
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.ui.widgets.RatingBar
import com.ifanr.tangzhi.ui.widgets.ScoreTextView
import java.text.NumberFormat

@EpoxyModelClass(layout = R.layout.related_products_item)
abstract class RelatedProductItemModel: EpoxyModelWithHolder<RelatedProductItemModel.Holder>() {

    @EpoxyAttribute
    lateinit var product: Product

    override fun bind(holder: Holder) {
        Glide.with(holder.ctx).roundedRect().load(product.coverImage).into(holder.coverIv)
        holder.titleTv.text = product.name
        holder.ratingbar.setProgress(product.rating)
        holder.ratingTv.setScore(product.rating)
        holder.infoTv.text = holder.ctx.getString(R.string.related_products_info,
            product.postCount, product.reviewCount)

        holder.view.setOnClickListener {
            ARouter.getInstance().build(Routes.product)
                .withString(Routes.productId, product.id)
                .navigation(holder.ctx)
        }
    }

    class Holder: KotlinEpoxyHolder() {
        val coverIv by bind<ImageView>(R.id.coverIv)
        val titleTv by bind<TextView>(R.id.titleTv)
        val ratingbar by bind<RatingBar>(R.id.ratingbar)
        val ratingTv by bind<ScoreTextView>(R.id.ratingTv)
        val infoTv by bind<TextView>(R.id.infoTv)
    }
}