package com.ifanr.tangzhi.ext

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun Fragment.toast(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.observeToastLiveData(liveData: LiveData<String>) {
    liveData.observe(this, Observer { it?.also { toast(it) } })
}