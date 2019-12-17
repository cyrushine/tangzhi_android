package com.ifanr.tangzhi.ui.productlist

import androidx.lifecycle.ViewModel
import com.ifanr.tangzhi.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProductListModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProductLIstViewModel::class)
    abstract fun bindProductListViewModel(vm: ProductLIstViewModel): ViewModel
}