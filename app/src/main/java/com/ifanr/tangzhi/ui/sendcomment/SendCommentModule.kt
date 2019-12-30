package com.ifanr.tangzhi.ui.sendcomment

import androidx.lifecycle.ViewModel
import com.ifanr.tangzhi.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SendCommentModule {

    @Binds
    @IntoMap
    @ViewModelKey(SendCommentViewModel::class)
    abstract fun bindSendCommentViewModel(vm: SendCommentViewModel): ViewModel

}