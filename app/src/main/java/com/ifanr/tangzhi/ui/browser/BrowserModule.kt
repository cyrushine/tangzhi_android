package com.ifanr.tangzhi.ui.browser

import androidx.lifecycle.ViewModel
import com.ifanr.tangzhi.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class BrowserModule {

    @Binds
    @IntoMap
    @ViewModelKey(BrowserViewModel::class)
    abstract fun bindBrowserViewModule(vm: BrowserViewModel): ViewModel

}