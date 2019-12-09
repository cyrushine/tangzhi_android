package com.ifanr.android.tangzhi.ui.product.posts

import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.ifanr.android.tangzhi.R
import com.ifanr.android.tangzhi.epoxy.KotlinEpoxyHolder
import com.ifanr.android.tangzhi.ext.rounded
import com.ifanr.android.tangzhi.ext.roundedRect
import com.ifanr.android.tangzhi.model.Product
import com.ifanr.android.tangzhi.ui.product.widgets.ProductTagsGroup

@EpoxyModelClass(layout = R.layout.product_posts_item)
abstract class PostModel: EpoxyModelWithHolder<PostModel.Holder>() {

    @EpoxyAttribute
    lateinit var data: Product.CachedPost

    override fun bind(holder: Holder) {
        Glide.with(holder.cover).roundedRect().load(data.postCoverImage).into(holder.cover)
        holder.title.text = data.postTitle
        Glide.with(holder.avatar).rounded().load(data.createdByAvatar).into(holder.avatar)
        holder.author.text = data.createdByName
        holder.tagsGroup.setTags(data.tag ?: emptyList())
    }

    class Holder: KotlinEpoxyHolder() {
        val cover by bind<ImageView>(R.id.coverIV)
        val title by bind<TextView>(R.id.titleTv)
        val tagsGroup by bind<ProductTagsGroup>(R.id.tagsGroup)
        val avatar by bind<ImageView>(R.id.avatarIV)
        val author by bind<TextView>(R.id.authorTv)
    }
}