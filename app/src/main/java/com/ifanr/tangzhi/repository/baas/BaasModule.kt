package com.ifanr.tangzhi.repository.baas

import com.ifanr.tangzhi.repository.baas.impl.BaasRepositoryImpl
import com.ifanr.tangzhi.repository.baas.impl.SettingRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class BaasModule {

    @Binds
    @Singleton
    abstract fun bindBaasRepository(impl: BaasRepositoryImpl): BaasRepository

    @Binds
    @Singleton
    abstract fun bindSettingRepository(impl: SettingRepositoryImpl): SettingRepository

}