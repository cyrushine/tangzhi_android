package com.ifanr.android.tangzhi.ext

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

inline fun <reified VM: ViewModel> FragmentActivity.viewModelOf(factory: ViewModelProvider.Factory) =
    ViewModelProviders.of(this, factory)[VM::class.java]