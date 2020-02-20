package com.ifanr.tangzhi.ui.base.model

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ui.base.epoxy.KotlinEpoxyHolder

/**
 * 默认的空白列表占位
 */
@EpoxyModelClass(layout = R.layout.blank_place_holder_model)
abstract class BlankPlaceHolderModel: EpoxyModelWithHolder<BlankPlaceHolderModel.Holder>() {

    class Holder: KotlinEpoxyHolder()
}