package com.ifanr.tangzhi.ui.sendreview

import androidx.lifecycle.ViewModel
import com.ifanr.tangzhi.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SendReviewModule {

    @Binds
    @IntoMap
    @ViewModelKey(SendReviewViewModel::class)
    abstract fun bindSendReviewViewModule(vm: SendReviewViewModel): ViewModel
}