package com.ifanr.android.tangzhi.ui.productparam

import androidx.lifecycle.ViewModel
import com.ifanr.android.tangzhi.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProductParamModule {

    @IntoMap
    @Binds
    @ViewModelKey(ProductParamViewModel::class)
    abstract fun bindProductParamViewModel(viewModel: ProductParamViewModel): ViewModel

}