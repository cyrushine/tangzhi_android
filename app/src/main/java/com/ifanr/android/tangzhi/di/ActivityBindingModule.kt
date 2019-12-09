package com.ifanr.android.tangzhi.di

import android.app.LauncherActivity
import com.ifanr.android.tangzhi.ui.LaunchActivity
import com.ifanr.android.tangzhi.ui.product.ProductActivity
import com.ifanr.android.tangzhi.ui.product.ProductModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ProductModule::class])
    abstract fun productActivity(): ProductActivity

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun launchActivity(): LaunchActivity
}