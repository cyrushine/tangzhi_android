package com.ifanr.tangzhi.ui.signin.wechat

import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.finishDelay
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ext.toast
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.statusBar
import com.ifanr.tangzhi.ui.widgets.observeLoadingLiveData
import kotlinx.android.synthetic.main.activity_sign_in.*

/**
 * 登录
 */
@Route(path = Routes.signInByWechat)
class SignInByWechatActivity : BaseViewModelActivity() {

    companion object {
        private const val TAG = "SignInActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        statusBar(whiteText = false)

        val vm: SignInByWechatViewModel = viewModel()
        observeLoadingLiveData(vm.loading)
        vm.event.observe(this, Observer {
            when (it) {

                SignInEvent.SignInSuccess -> {
                    finish()
                }

                is SignInEvent.SignInFail ->
                    toast("${getString(R.string.sign_in_fail)}(${it.ex?.message})")

                SignInEvent.PhoneNotBind -> {
                    ARouter.getInstance().build(Routes.bindLocalPhone).navigation(this)
                    finishDelay()
                }
            }
        })

        wechat.setOnClickListener {
            vm.signInByWechat()
        }

        phone.setOnClickListener {
            ARouter.getInstance().build(Routes.signInByPhone).navigation(this)
            finishDelay()
        }

        email.setOnClickListener {
            ARouter.getInstance().build(Routes.signInByEmail).navigation(this)
            finishDelay()
        }

        close.setOnClickListener { finish() }
    }
}
