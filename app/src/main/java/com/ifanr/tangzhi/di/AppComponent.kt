package com.ifanr.tangzhi.di

import com.ifanr.tangzhi.App
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