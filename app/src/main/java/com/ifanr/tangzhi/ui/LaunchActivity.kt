package com.ifanr.tangzhi.ui

import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.delay
import com.ifanr.tangzhi.ext.finishDelay
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseActivity
import com.minapp.android.sdk.auth.Auth

class LaunchActivity : BaseActivity() {

    companion object {
        private const val TAG = "LaunchActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        statusBar(whiteText = false)

        delay(1000L) {
            Auth.logout()
            ARouter.getInstance().build(Routes.signInByWechat).navigation(this)
            finishDelay()
        }
    }
}
