package com.ifanr.tangzhi.ui.updateprofile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.route.Extra
import com.ifanr.tangzhi.route.Routes

/**
 * 编辑个人资料
 */
@Route(path = Routes.updateProfile, extras = Extra.signIn)
class UpdateProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
    }
}
