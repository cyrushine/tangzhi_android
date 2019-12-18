package com.ifanr.tangzhi.ext

import android.content.pm.PackageManager

fun IntArray.permissionGranted() =
    this.all { it == PackageManager.PERMISSION_GRANTED }