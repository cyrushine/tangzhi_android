package com.ifanr.tangzhi.ui.points

import androidx.lifecycle.ViewModel
import com.ifanr.tangzhi.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PointsModule {

    @IntoMap
    @Binds
    @ViewModelKey(PointsViewModel::class)
    abstract fun bindPointsViewModel(vm: PointsViewModel): ViewModel
}