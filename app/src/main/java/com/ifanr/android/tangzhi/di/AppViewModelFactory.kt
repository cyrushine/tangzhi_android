package com.ifanr.android.tangzhi.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class AppViewModelFactory @Inject constructor(
    private val creator: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val provider = creator.entries.find { modelClass.isAssignableFrom(it.key) }?.value
            ?: throw IllegalArgumentException("unknow ViewModel class: $modelClass")
        return provider.get() as T
    }

}