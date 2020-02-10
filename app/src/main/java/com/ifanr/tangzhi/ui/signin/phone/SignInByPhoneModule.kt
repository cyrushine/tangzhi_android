package com.ifanr.tangzhi.ui.signin.phone

import androidx.lifecycle.ViewModel
import com.ifanr.tangzhi.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SignInByPhoneModule {

    @IntoMap
    @Binds
    @ViewModelKey(SignInByPhoneViewModel::class)
    abstract fun bindSignInByPhoneViewModel(vm: SignInByPhoneViewModel): ViewModel

}