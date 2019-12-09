package com.ifanr.android.tangzhi.ui.product

import androidx.lifecycle.ViewModel
import com.ifanr.android.tangzhi.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProductModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProductViewModel::class)
    abstract fun bindProductViewModule(viewModel: ProductViewModel): ViewModel

}