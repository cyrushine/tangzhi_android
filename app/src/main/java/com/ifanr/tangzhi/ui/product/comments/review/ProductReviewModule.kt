package com.ifanr.tangzhi.ui.product.comments.review

import androidx.lifecycle.ViewModel
import com.ifanr.tangzhi.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProductReviewModule {

    @Binds
    @IntoMap
    @ViewModelKey(ReviewViewModel::class)
    abstract fun bindProductReviewModule(vm: ReviewViewModel): ViewModel

}