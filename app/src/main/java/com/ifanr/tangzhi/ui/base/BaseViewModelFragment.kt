package com.ifanr.tangzhi.ui.base

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import javax.inject.Inject

abstract class BaseViewModelFragment: BaseFragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
}

inline fun <reified T: BaseViewModel> BaseViewModelFragment.viewModelOf(
    activity: FragmentActivity? = null
): T =
    if (activity != null)
        ViewModelProviders.of(activity, factory)[T::class.java]
    else
        ViewModelProviders.of(this, factory)[T::class.java]