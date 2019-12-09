package com.ifanr.android.tangzhi.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.*

abstract class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
        AndroidInjection.inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFindViewByIdCache()
    }
}