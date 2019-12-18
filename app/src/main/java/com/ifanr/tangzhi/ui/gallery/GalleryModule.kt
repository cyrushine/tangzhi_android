package com.ifanr.tangzhi.ui.gallery

import androidx.lifecycle.ViewModel
import com.ifanr.tangzhi.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class GalleryModule {

    @ViewModelKey(GalleryViewModel::class)
    @IntoMap
    @Binds
    abstract fun bindGalleryViewModel(vm: GalleryViewModel): ViewModel

}