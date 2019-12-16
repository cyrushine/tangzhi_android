package com.ifanr.tangzhi.ui.product.list

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.epoxy.KotlinEpoxyHolder
import com.ifanr.tangzhi.ext.into
import com.ifanr.tangzhi.ext.roundedRect
import com.ifanr.tangzhi.model.ProductList
import com.ifanr.tangzhi.ui.widgets.StackImageView

@EpoxyModelClass(layout = R.layout.product_list_item)
abstract class ProductListModel: EpoxyModelWithHolder<ProductListModel.Holder>() {

    @EpoxyAttribute
    lateinit var data: ProductList

    override fun bind(holder: Holder) {
        Glide.with(holder.ctx).roundedRect().load(data.coverImage).into(holder.coverIv)
        holder.nameTv.text = data.name
        holder.countTv.text = holder.ctx.getString(R.string.product_list_count, data.items.size)
    }

    class Holder: KotlinEpoxyHolder() {
        val coverIv by bind<StackImageView>(R.id.coverIv)
        val nameTv by bind<TextView>(R.id.nameTv)
        val countTv by bind<TextView>(R.id.countTv)
    }
}