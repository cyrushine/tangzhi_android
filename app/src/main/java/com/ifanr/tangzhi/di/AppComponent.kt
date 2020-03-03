package com.ifanr.tangzhi.di

import com.ifanr.tangzhi.App
import com.ifanr.tangzhi.repository.baas.BaasModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.*
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    BaasModule::class,
    ViewModelFactoryModule::class,
    ActivityBindingModule::class,
    AppBindModule::class,
    AppProviderModule::class,
    GlideModule::class
])
interface AppComponent: AndroidInjector<App> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: App): AppComponent
    }

}