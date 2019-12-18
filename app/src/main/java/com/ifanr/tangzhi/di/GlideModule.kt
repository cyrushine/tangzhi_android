package com.ifanr.tangzhi.di

import android.app.Application
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides

@Module
class GlideModule {

    @Provides
    fun provideRequestManager(application: Application): RequestManager =
        Glide.with(application)
}