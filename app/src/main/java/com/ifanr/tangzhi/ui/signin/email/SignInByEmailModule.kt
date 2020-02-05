package com.ifanr.tangzhi.ui.signin.email

import androidx.lifecycle.ViewModel
import com.ifanr.tangzhi.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SignInByEmailModule {

    @Binds
    @IntoMap
    @ViewModelKey(SignInByEmailViewModel::class)
    abstract fun bindViewModel(vm: SignInByEmailViewModel): ViewModel

}