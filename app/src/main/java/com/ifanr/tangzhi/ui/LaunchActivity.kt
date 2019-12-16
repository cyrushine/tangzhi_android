package com.ifanr.tangzhi.ui

import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.Routes
import com.ifanr.tangzhi.ui.base.BaseActivity
import com.uber.autodispose.android.lifecycle.autoDispose
import io.reactivex.Observable
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
                    .withString(Routes.productId, "5d5520dffb8b2726cdfafe89")
                    .navigation(this)
            }
    }
}
