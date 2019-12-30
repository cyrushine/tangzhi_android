package com.ifanr.tangzhi.ui.base.model

import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithView

@EpoxyModelClass
abstract class BlankModel: EpoxyModelWithView<View>() {
    override fun buildView(parent: ViewGroup): View {
        return View(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(0, 0)
        }
    }
}