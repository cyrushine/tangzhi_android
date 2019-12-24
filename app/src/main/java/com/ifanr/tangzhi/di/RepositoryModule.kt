package com.ifanr.tangzhi.di

import com.ifanr.tangzhi.repository.baas.BaaSRepositoryModule
import dagger.Module

@Module(includes = [BaaSRepositoryModule::class])
abstract class RepositoryModule