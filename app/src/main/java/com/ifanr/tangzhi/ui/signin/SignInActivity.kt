package com.ifanr.tangzhi.ui.signin

import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.ifanr.tangzhi.Event
import com.ifanr.tangzhi.EventBus
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ext.toast
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.statusBar
import com.minapp.android.sdk.auth.Auth
import com.minapp.android.sdk.wechat.WechatComponent
import com.minapp.android.sdk.wechat.WechatSignInCallback
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

/**
 * 登录
 */
@Route(path = Routes.signIn)
class SignInActivity : BaseViewModelActivity() {

    companion object {
        private const val TAG = "SignInActivity"
    }

    @Inject
    lateinit var bus: EventBus

    private val wechatCallback = object: WechatSignInCallback {
        override fun onSuccess() {
            toast("微信登录成功")
            bus.post(Event.SignIn)
            finish()
        }

        override fun onFailure(ex: Exception?) {
            Log.e(TAG, ex?.message, ex)
            toast("登录失败：${ex?.message}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        statusBar(whiteText = false)

        wechatBtn.setOnClickListener {
            WechatComponent.signIn(wechatCallback)
        }
    }
}
