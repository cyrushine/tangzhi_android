package com.ifanr.tangzhi.ui.base.model

import android.widget.TextView
import androidx.annotation.StringRes
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.list_header_count)
abstract class ListHeaderCountModel: EpoxyModelWithHolder<ListHeaderCountModel.Holder>() {

    @EpoxyAttribute
    @StringRes
    var stringRes = 0

    @EpoxyAttribute
    var count = 0

    override fun bind(holder: Holder) {
        holder.countTv.text = holder.ctx.getString(stringRes, count)
    }

    class Holder: KotlinEpoxyHolder() {
        val countTv by bind<TextView>(R.id.countTv)
    }
}