package com.ifanr.android.tangzhi.di

import com.ifanr.android.tangzhi.repository.product.ProductRepositoryModule
import dagger.Module

@Module(includes = [ProductRepositoryModule::class])
abstract class RepositoryModule