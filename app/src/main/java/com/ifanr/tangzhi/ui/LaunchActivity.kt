package com.ifanr.tangzhi.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.service.ProfileService
import com.ifanr.tangzhi.ui.base.BaseActivity
import com.minapp.android.sdk.auth.Auth
import kotlinx.android.synthetic.main.activity_launch.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LaunchActivity : BaseActivity() {

    companion object {
        private const val TAG = "LaunchActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        statusBar(whiteText = false)

        Auth.logout()
        ARouter.getInstance().build(Routes.signIn).navigation(this)
        ProfileService.start(this)
        finish()
    }
}
