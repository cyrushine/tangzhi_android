package com.ifanr.tangzhi.repository.baas

import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class BaasRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBaasRepository(impl: BaasRepositoryImpl): BaasRepository

}