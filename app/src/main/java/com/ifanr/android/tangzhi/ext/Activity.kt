package com.ifanr.android.tangzhi.ext

import android.app.Activity
import android.widget.Toast
import androidx.annotation.StringRes

fun Activity.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Activity.toast(@StringRes res: Int) {
    Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
}