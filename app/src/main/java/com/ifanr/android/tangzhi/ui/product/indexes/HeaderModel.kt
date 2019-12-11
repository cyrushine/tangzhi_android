package com.ifanr.android.tangzhi.ui.product.indexes

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.ifanr.android.tangzhi.R
import com.ifanr.android.tangzhi.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.product_indexes_dialog_header)
abstract class HeaderModel: EpoxyModelWithHolder<HeaderModel.Holder>() {

    @EpoxyAttribute
    lateinit var name: String

    @EpoxyAttribute
    var score = 0f

    override fun bind(holder: Holder) {
        holder.score.setScore(score)
        holder.nameTv.text = name
    }

    class Holder: KotlinEpoxyHolder() {
        val score by bind<IndexesBox>(R.id.score)
        val nameTv by bind<TextView>(R.id.nameTv)
    }
}