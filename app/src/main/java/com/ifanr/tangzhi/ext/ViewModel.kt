package com.ifanr.tangzhi.ext

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

inline fun <reified VM: ViewModel> FragmentActivity.viewModelOf(factory: ViewModelProvider.Factory) =
    ViewModelProviders.of(this, factory)[VM::class.java]