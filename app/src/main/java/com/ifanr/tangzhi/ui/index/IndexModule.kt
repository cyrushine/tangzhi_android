package com.ifanr.tangzhi.ui.index

import androidx.lifecycle.ViewModel
import com.ifanr.tangzhi.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class IndexModule {

    @Binds
    @IntoMap
    @ViewModelKey(IndexViewModel::class)
    abstract fun bindIndexViewModule(vm: IndexViewModel): ViewModel

}