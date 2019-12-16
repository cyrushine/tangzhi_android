package com.ifanr.tangzhi.ui.relatedproducts

import androidx.lifecycle.ViewModel
import com.ifanr.tangzhi.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RelatedProductsModule {

    @ViewModelKey(RelatedProductsViewModel::class)
    @IntoMap
    @Binds
    abstract fun bindRelatedProductsViewModel(vm: RelatedProductsViewModel): ViewModel
}