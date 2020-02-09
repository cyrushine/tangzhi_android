package com.ifanr.tangzhi.ui.signin.bindlocalphone

import androidx.lifecycle.ViewModel
import com.ifanr.tangzhi.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class BindLocalPhoneModule {

    @Binds
    @IntoMap
    @ViewModelKey(BindLocalPhoneViewModel::class)
    abstract fun bindViewModel(vm: BindLocalPhoneViewModel): ViewModel

}