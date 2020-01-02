package com.ifanr.tangzhi.di

import com.ifanr.tangzhi.repository.baas.BaasRepositoryModule
import dagger.Module

@Module(includes = [BaasRepositoryModule::class])
abstract class RepositoryModule