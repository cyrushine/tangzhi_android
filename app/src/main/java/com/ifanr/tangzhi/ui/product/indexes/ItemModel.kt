package com.ifanr.tangzhi.ui.product.indexes

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.epoxy.KotlinEpoxyHolder
import com.ifanr.tangzhi.ui.widgets.ScoreTextView

@EpoxyModelClass(layout = R.layout.product_indexes_dialog_item)
abstract class ItemModel: EpoxyModelWithHolder<ItemModel.Holder>() {

    @EpoxyAttribute
    lateinit var data: ProductRating.RatingRecord

    override fun bind(holder: Holder) {
        holder.nameTv.text = data.name
        holder.scoreTv.setScore(data.rating)
    }

    class Holder: KotlinEpoxyHolder() {
        val nameTv by bind<TextView>(R.id.nameTv)
        val scoreTv by bind<ScoreTextView>(R.id.scoreTv)
    }
}