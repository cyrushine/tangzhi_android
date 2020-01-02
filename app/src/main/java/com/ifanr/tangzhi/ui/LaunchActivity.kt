package com.ifanr.tangzhi.ui

import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.Routes
import com.ifanr.tangzhi.ext.toast
import com.ifanr.tangzhi.ui.base.BaseActivity
import com.minapp.android.sdk.auth.Auth
import com.uber.autodispose.android.lifecycle.autoDispose
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_launch.*
import java.util.concurrent.TimeUnit

class LaunchActivity : BaseActivity() {

    companion object {
        private const val TAG = "LaunchActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        ARouter.getInstance().build(Routes.product)
            .withString(Routes.productId, "5db8518a6f40dd69b311ffc1")
            .navigation(this)

        /*ARouter.getInstance().build(Routes.index).navigation(this)*/

        alloc.setOnClickListener {
            ARouter.getInstance().build(Routes.share)
                .navigation(this)
        }
    }
}
