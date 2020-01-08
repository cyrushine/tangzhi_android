package com.ifanr.tangzhi.ui.updateprofile

import androidx.lifecycle.ViewModel
import com.ifanr.tangzhi.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class UpdateProfileModule {

    @IntoMap
    @Binds
    @ViewModelKey(UpdateProfileViewModel::class)
    abstract fun bindUpdateProfileViewModel(vm: UpdateProfileViewModel): ViewModel
}