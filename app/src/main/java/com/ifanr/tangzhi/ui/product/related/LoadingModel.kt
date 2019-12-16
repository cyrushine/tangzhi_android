package com.ifanr.tangzhi.ui.product.related

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyModelWithView
import com.ifanr.tangzhi.R

class LoadingModel: EpoxyModelWithView<View>() {

    companion object {
        private const val LAYOUT = R.layout.product_related_loading
    }

    override fun getViewType(): Int {
        return LAYOUT
    }

    override fun buildView(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(LAYOUT, parent, false)
    }

}