package com.ifanr.android.tangzhi.ui.product.posts

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.ifanr.android.tangzhi.R
import com.ifanr.android.tangzhi.epoxy.KotlinEpoxyHolder
import kotlin.properties.Delegates

@EpoxyModelClass(layout = R.layout.product_post_header)
abstract class PostHeaderModel: EpoxyModelWithHolder<PostHeaderModel.Holder>() {

    @EpoxyAttribute
    var total = 0L

    override fun bind(holder: Holder) {
        holder.totalTv.text = holder.totalTv.context.getString(R.string.product_posts_total, total)
    }

    override fun isShown(): Boolean {
        return total > 0L
    }

    class Holder: KotlinEpoxyHolder() {
        val totalTv by bind<TextView>(R.id.totalTv)
    }
}