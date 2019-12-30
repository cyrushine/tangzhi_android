package com.ifanr.tangzhi.ui.base.model

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.list_end_slogon)
abstract class ListEndSlogonModel: EpoxyModelWithHolder<ListEndSlogonModel.Holder>() {

    class Holder: KotlinEpoxyHolder()
}