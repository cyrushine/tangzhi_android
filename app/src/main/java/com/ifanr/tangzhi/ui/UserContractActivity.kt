package com.ifanr.tangzhi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.route.Routes
import kotlinx.android.synthetic.main.activity_user_contract.*

/**
 * 用户协议
 */
@Route(path = Routes.userContract)
class UserContractActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_contract)
        statusBar(whiteText = false)

        toolbar.close.setOnClickListener { finish() }
    }
}
