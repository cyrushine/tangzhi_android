package com.ifanr.tangzhi.di

import android.app.Application
import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.ifanr.tangzhi.App
import com.ifanr.tangzhi.AppConfig
import com.ifanr.tangzhi.AppConfigImpl
import com.ifanr.tangzhi.EventBus
import com.ifanr.tangzhi.appmgr.AppMgr
import com.ifanr.tangzhi.appmgr.AppMgrImpl
import com.ifanr.tangzhi.repository.ifanr.IfanrServerRepository
import com.ifanr.tangzhi.repository.ifanr.IfanrServerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class AppBindModule {

    @Binds
    abstract fun bindApplication(app: App): Application

    @Binds
    abstract fun bindAppConfig(config: AppConfigImpl): AppConfig

    @Binds
    abstract fun bindContext(app: App): Context

    @Singleton
    @Binds
    abstract fun bindAppMgr(mgrImpl: AppMgrImpl): AppMgr

    @Singleton
    @Binds
    abstract fun bindIfanrServerRepository(impl: IfanrServerRepositoryImpl): IfanrServerRepository
}