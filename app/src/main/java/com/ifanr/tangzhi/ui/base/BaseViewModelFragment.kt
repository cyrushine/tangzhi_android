package com.ifanr.tangzhi.ui.base

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import javax.inject.Inject

abstract class BaseViewModelFragment: BaseFragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
}

inline fun <reified T: BaseViewModel> BaseViewModelFragment.viewModelOf(): T =
    ViewModelProviders.of(this, factory)[T::class.java]