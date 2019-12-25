package com.ifanr.tangzhi.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.*

abstract class BaseFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFindViewByIdCache()
    }
}