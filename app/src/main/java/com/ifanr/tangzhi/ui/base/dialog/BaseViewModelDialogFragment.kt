package com.ifanr.tangzhi.ui.base.dialog

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ifanr.tangzhi.ui.base.BaseViewModel
import javax.inject.Inject

abstract class BaseViewModelDialogFragment: BaseDialogFragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
}

inline fun <reified T: BaseViewModel> BaseViewModelDialogFragment.viewModelOf(
    activity: FragmentActivity? = null
): T =
    if (activity != null)
        ViewModelProviders.of(activity, factory)[T::class.java]
    else
        ViewModelProviders.of(this, factory)[T::class.java]