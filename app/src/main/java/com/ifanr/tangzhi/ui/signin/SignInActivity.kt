package com.ifanr.tangzhi.ui.signin

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.Event
import com.ifanr.tangzhi.EventBus
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.delay
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ext.toast
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.statusBar
import com.ifanr.tangzhi.ui.widgets.dismissLoading
import com.ifanr.tangzhi.ui.widgets.observeLoadingLiveData
import com.ifanr.tangzhi.ui.widgets.showLoading
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        statusBar(whiteText = false)

        val vm: SignInViewModel = viewModel()
        observeLoadingLiveData(vm.loading)
        vm.event.observe(this, Observer {
            when (it) {
                SignInEvent.SignInSuccess -> finish()
                is SignInEvent.SignInFail ->
                    toast("${getString(R.string.sign_in_fail)}(${it.ex?.message})")
            }
        })

        wechat.setOnClickListener {
            vm.signInByWechat()
        }

        phone.setOnClickListener {
            ARouter.getInstance().build(Routes.signInByPhone).navigation(this)
            finish()
        }

        email.setOnClickListener {
            ARouter.getInstance().build(Routes.signInByEmail).navigation(this)
            finish()
        }

        close.setOnClickListener { finish() }
    }
}
