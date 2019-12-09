package com.ifanr.android.tangzhi.di

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import com.ifanr.android.tangzhi.App
import com.ifanr.android.tangzhi.repository.product.ProductRepository
import dagger.BindsInstance
import dagger.Component
import dagger.android.*
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    RepositoryModule::class,
    ViewModelFactoryModule::class,
    ActivityBindingModule::class
])
interface AppComponent: AndroidInjector<App> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: App): AppComponent
    }

}