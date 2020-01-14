package com.ifanr.tangzhi.ui.share

import androidx.lifecycle.ViewModel
import com.ifanr.tangzhi.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ShareModule {

    @Binds
    @IntoMap
    @ViewModelKey(ShareViewModel::class)
    abstract fun bindShareViewModel(vm: ShareViewModel): ViewModel

}