package com.ifanr.tangzhi.di

import com.ifanr.tangzhi.repository.product.ProductRepositoryModule
import dagger.Module

@Module(includes = [ProductRepositoryModule::class])
abstract class RepositoryModule