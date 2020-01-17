package com.ifanr.tangzhi.ext

import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ifanr.tangzhi.R
import com.uber.autodispose.android.lifecycle.autoDispose
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

fun Activity.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Activity.toastFail(msg: String?) {
    toast("${getString(R.string.operation_error)}$msg")
}

fun Activity.toast(@StringRes res: Int) {
    Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.observeToast(msg: MutableLiveData<String>) {
    msg.observe(this, Observer { it?.also { toast(it) }})
}

fun Activity.checkAndRequestPermissions(permission: Array<String>, requestCode: Int): Boolean {
    val permissionGranted = permission
        .map { ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }
        .all { it }
    if (!permissionGranted)
        ActivityCompat.requestPermissions(this, permission, requestCode)
    return permissionGranted
}

/**
 * @param ms MILLISECONDS
 */
fun AppCompatActivity.delay(ms: Long, block: () -> Unit) {
    Completable.timer(ms, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .autoDispose(this)
        .subscribe { block.invoke() }
}