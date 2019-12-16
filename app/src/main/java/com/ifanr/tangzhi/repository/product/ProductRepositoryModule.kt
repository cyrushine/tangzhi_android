package com.ifanr.tangzhi.repository.product

import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class ProductRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository

}