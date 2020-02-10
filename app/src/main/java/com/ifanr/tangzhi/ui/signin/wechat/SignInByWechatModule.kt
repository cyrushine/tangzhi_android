package com.ifanr.tangzhi.ui.signin.wechat

import androidx.lifecycle.ViewModel
import com.ifanr.tangzhi.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SignInByWechatModule {

    @Binds
    @IntoMap
    @ViewModelKey(SignInByWechatViewModel::class)
    abstract fun bindSignInViewModel(vm: SignInByWechatViewModel): ViewModel
}