package com.ifanr.tangzhi.ui.postlist

import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.epoxy.KotlinEpoxyHolder
import com.ifanr.tangzhi.ext.roundedRect
import com.ifanr.tangzhi.model.Product

@EpoxyModelClass(layout = R.layout.postlist_item)
abstract class PostListItemModel: EpoxyModelWithHolder<PostListItemModel.Holder>() {

    @EpoxyAttribute
    lateinit var post: Product.CachedPost

    override fun bind(holder: Holder) {
        Glide.with(holder.ctx).roundedRect().load(post.postCoverImage).into(holder.coverIv)
        holder.titleTv.text = post.postTitle
        holder.authorTv.text = post.createdByName
        holder.view.setOnClickListener {
            ARouter.getInstance().build(Routes.browser)
                .withString(Routes.browserTitle, post.postTitle)
                .withString(Routes.browserUrl, "https://www.ifanr.com/${post.postId}")
                .navigation(it.context)
        }
    }

    class Holder: KotlinEpoxyHolder() {
        val coverIv by bind<ImageView>(R.id.coverIv)
        val titleTv by bind<TextView>(R.id.titleTv)
        val authorTv by bind<TextView>(R.id.authorTv)
    }
}