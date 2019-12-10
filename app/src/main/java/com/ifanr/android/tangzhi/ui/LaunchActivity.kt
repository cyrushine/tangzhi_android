package com.ifanr.android.tangzhi.ui

import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.android.tangzhi.R
import com.ifanr.android.tangzhi.Routes
import com.ifanr.android.tangzhi.ui.base.BaseActivity
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.android.lifecycle.autoDispose
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_launch.*
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

class LaunchActivity : BaseActivity() {

    companion object {
        private const val TAG = "6"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        Observable.timer(500, TimeUnit.MILLISECONDS)
            .autoDispose(this)
            .subscribe {
                ARouter.getInstance().build(Routes.product)
                    .withString(Routes.productId, "5cac48ce2691bb27f7fd5869")
                    .navigation(this)
            }
    }
}
