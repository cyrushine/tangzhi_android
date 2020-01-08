package com.ifanr.tangzhi.di

import android.app.Application
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.ifanr.tangzhi.App
import com.ifanr.tangzhi.AppConfig
import com.ifanr.tangzhi.AppConfigImpl
import com.ifanr.tangzhi.EventBus
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Binds
    abstract fun bindApplication(app: App): Application

    @Binds
    abstract fun bindAppConfig(config: AppConfigImpl): AppConfig

    @Binds
    abstract fun bindEventBus(bus: EventBus): EventBus
}