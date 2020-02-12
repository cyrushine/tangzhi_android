package com.ifanr.tangzhi.ui.follows

import androidx.lifecycle.ViewModel
import com.ifanr.tangzhi.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FollowsModule {

    @Binds
    @IntoMap
    @ViewModelKey(FollowsViewModel::class)
    abstract fun bindViewModel(vm: FollowsViewModel): ViewModel

}