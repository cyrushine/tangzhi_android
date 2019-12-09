package com.ifanr.android.tangzhi.ui.product.related

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.airbnb.epoxy.EpoxyModelWithView
import com.ifanr.android.tangzhi.R
import com.ifanr.android.tangzhi.epoxy.KotlinEpoxyHolder

class LoadingModel: EpoxyModelWithView<View>() {

    companion object {
        private val LAYOUT = R.layout.product_related_loading
    }

    override fun getViewType(): Int {
        return LAYOUT
    }

    override fun buildView(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context)
            .inflate(LAYOUT, parent, false)
    }

}