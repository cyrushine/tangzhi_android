package com.ifanr.tangzhi.ui.usercontract

import androidx.lifecycle.ViewModel
import com.ifanr.tangzhi.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class UserContractModule {

    @Binds
    @IntoMap
    @ViewModelKey(UserContractViewModel::class)
    abstract fun bindViewModel(vm: UserContractViewModel): ViewModel
}