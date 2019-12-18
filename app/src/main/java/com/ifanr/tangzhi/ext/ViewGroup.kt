package com.ifanr.tangzhi.ext

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflateInto(@LayoutRes layout: Int) =
    LayoutInflater.from(context).inflate(layout, this, true)