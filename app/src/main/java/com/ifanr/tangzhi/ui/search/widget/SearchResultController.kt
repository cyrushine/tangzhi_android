package com.ifanr.tangzhi.ui.search.widget

import android.graphics.Rect
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.*
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.epoxy.KotlinEpoxyHolder
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.ext.roundedRect
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.ui.base.BaseItemDecoration
import com.ifanr.tangzhi.ui.base.model.BasePagedListController
import com.ifanr.tangzhi.ui.base.model.ListEndSlogonModel_
import com.ifanr.tangzhi.ui.widgets.RatingBar
import com.ifanr.tangzhi.ui.widgets.ScoreTextView

class SearchResultController: BasePagedListController<Product>(
    itemDiffCallback = Product.DIFF_CALLBACK
) {

    @AutoModel
    lateinit var end: ListEndSlogonModel_

    override fun buildItemModel(currentPosition: Int, item: Product?): EpoxyModel<*> {
        val product = item ?: Product()
        return SearchItemModel_().apply {
            id(product.id)
            data(product)
        }
    }

}

@EpoxyModelClass(layout = R.layout.search_item)
abstract class SearchItemModel: EpoxyModelWithHolder<SearchItemModel.Holder>() {

    @EpoxyAttribute
    lateinit var data: Product

    override fun bind(holder: Holder) {
        Glide.with(holder.cover).roundedRect().load(data.coverImage).into(holder.cover)
        holder.name.text = data.name
        holder.ratingBar.setProgress(data.rating)
        holder.score.setScore(data.rating)
        holder.info.text = holder.ctx.getString(
            R.string.search_item_info, data.postCount, data.reviewCount)

        holder.view.setOnClickListener {
            ARouter.getInstance().build(Routes.product)
                .withString(Routes.productId, data.id)
                .navigation(holder.ctx)
        }
    }

    class Holder: KotlinEpoxyHolder() {
        val cover by bind<ImageView>(R.id.cover)
        val name by bind<TextView>(R.id.name)
        val ratingBar by bind<RatingBar>(R.id.ratingBar)
        val score by bind<ScoreTextView>(R.id.score)
        val info by bind<TextView>(R.id.info)
    }
}

class SearchResultDecoration: BaseItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        vh: RecyclerView.ViewHolder,
        vt: Int,
        position: Int
    ) {
        when (vt) {
            R.layout.search_item -> {
                outRect.top = parent.context.dp2px(10)
            }
        }
    }
}