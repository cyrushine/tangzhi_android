package com.ifanr.tangzhi.ui.base.dialog

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import dagger.android.support.AndroidSupportInjection

abstract class BaseDialogFragment: DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }
}