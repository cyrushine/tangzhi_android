package com.ifanr.tangzhi.ui.product.list

import android.graphics.Typeface
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.epoxy.KotlinEpoxyHolder
import com.ifanr.tangzhi.ext.getColorCompat
import com.ifanr.tangzhi.ext.into
import com.ifanr.tangzhi.ext.roundedRect
import com.ifanr.tangzhi.model.ProductList
import com.ifanr.tangzhi.ui.widgets.StackImageView

@EpoxyModelClass(layout = R.layout.product_list_item)
abstract class ProductListModel: EpoxyModelWithHolder<ProductListModel.Holder>() {

    @EpoxyAttribute
    lateinit var data: ProductList

    @EpoxyAttribute
    var style = Style.PRODUCT_PAGE

    override fun bind(holder: Holder) {
        val ctx = holder.ctx
        when (style) {
            Style.PRODUCT_PAGE -> {
                holder.coverIv.bgIv.setImageResource(R.drawable.stack_image_view)
                holder.nameTv.setTextColor(ctx.getColorCompat(R.color.white))
                holder.nameTv.typeface = Typeface.DEFAULT
            }

            Style.PRODUCT_LIST -> {
                holder.coverIv.bgIv.setImageResource(R.drawable.stack_image_view_grey)
                holder.nameTv.setTextColor(ctx.getColorCompat(R.color.base_12))
                holder.nameTv.typeface = Typeface.DEFAULT_BOLD
            }
        }

        Glide.with(ctx).roundedRect().load(data.coverImage).into(holder.coverIv)
        holder.nameTv.text = data.name
        holder.countTv.text = ctx.getString(R.string.product_list_count, data.items.size)
    }

    class Holder: KotlinEpoxyHolder() {
        val coverIv by bind<StackImageView>(R.id.coverIv)
        val nameTv by bind<TextView>(R.id.nameTv)
        val countTv by bind<TextView>(R.id.countTv)
    }

    enum class Style {
        PRODUCT_PAGE, // 产品说明书
        PRODUCT_LIST  // 清单列表
    }
}