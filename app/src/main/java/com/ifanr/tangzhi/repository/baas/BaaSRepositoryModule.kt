package com.ifanr.tangzhi.repository.baas

import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class BaaSRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBaaSRepository(impl: BaaSRepositoryImpl): BaaSRepository

}