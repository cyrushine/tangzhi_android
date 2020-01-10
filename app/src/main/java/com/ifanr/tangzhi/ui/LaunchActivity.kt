package com.ifanr.tangzhi.ui

import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseActivity
import com.minapp.android.sdk.auth.Auth
import kotlinx.android.synthetic.main.activity_launch.*

class LaunchActivity : BaseActivity() {

    companion object {
        private const val TAG = "LaunchActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        /*ARouter.getInstance().build(Routes.product)
            .withString(Routes.productId, "5db8518a6f40dd69b311ffc1")
            .navigation(this)*/
        ARouter.getInstance().build(Routes.index).navigation(this)

        alloc.setOnClickListener {
            ARouter.getInstance().build(Routes.share)
                .navigation(this)
        }
    }
}
