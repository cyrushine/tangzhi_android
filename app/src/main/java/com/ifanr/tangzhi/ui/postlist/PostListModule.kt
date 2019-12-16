package com.ifanr.tangzhi.ui.postlist

import androidx.lifecycle.ViewModel
import com.ifanr.tangzhi.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PostListModule {

    @Binds
    @IntoMap
    @ViewModelKey(PostListViewModel::class)
    abstract fun bindPostListViewModel(vm: PostListViewModel): ViewModel

}