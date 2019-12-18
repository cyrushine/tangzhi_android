package com.ifanr.tangzhi.ui.base

import androidx.lifecycle.ViewModelProvider
import com.ifanr.tangzhi.ext.viewModelOf
import javax.inject.Inject

abstract class BaseViewModelActivity: BaseActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

}

inline fun <reified T : BaseViewModel> BaseViewModelActivity.viewModel(): T =
    viewModelOf(factory)