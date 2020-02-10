package com.ifanr.tangzhi.ui.signin.bindphone

import androidx.lifecycle.ViewModel
import com.ifanr.tangzhi.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class BindPhoneModule {

    @Binds
    @IntoMap
    @ViewModelKey(BindPhoneViewModel::class)
    abstract fun bindViewModel(vm: BindPhoneViewModel): ViewModel

}