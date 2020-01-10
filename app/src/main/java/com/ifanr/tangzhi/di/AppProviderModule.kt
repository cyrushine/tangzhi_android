package com.ifanr.tangzhi.di

import android.content.Context
import android.view.inputmethod.InputMethodManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppProviderModule {

    @Provides
    @Singleton
    fun provideInputMethodManager(ctx: Context): InputMethodManager =
        ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
}