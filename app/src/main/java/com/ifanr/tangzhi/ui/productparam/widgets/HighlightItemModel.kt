package com.ifanr.tangzhi.ui.productparam.widgets

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.epoxy.KotlinEpoxyHolder
import com.ifanr.tangzhi.ui.productparam.Params

@EpoxyModelClass(layout = R.layout.product_param_highlight)
abstract class HighlightItemModel: EpoxyModelWithHolder<HighlightItemModel.Holder>() {

    @EpoxyAttribute
    lateinit var param: Params.Param

    override fun bind(holder: Holder) {
        holder.key.text = param.key
        holder.value.text = param.value
    }

    class Holder: KotlinEpoxyHolder() {
        val key by bind<TextView>(R.id.key)
        val value by bind<TextView>(R.id.value)
    }
}