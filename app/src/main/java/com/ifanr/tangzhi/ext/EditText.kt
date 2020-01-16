package com.ifanr.tangzhi.ext

import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun EditText.bind(owner: LifecycleOwner, liveData: MutableLiveData<String>) {
    addTextChangedListener(afterTextChanged = { liveData.value = it.toString().trim() })
}