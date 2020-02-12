package com.ifanr.tangzhi.ui.message.fragment

import androidx.lifecycle.ViewModel
import com.ifanr.tangzhi.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MessagePageModule {

    @Binds
    @IntoMap
    @ViewModelKey(MessagePageViewModel::class)
    abstract fun bindViewModel(vm: MessagePageViewModel): ViewModel

}