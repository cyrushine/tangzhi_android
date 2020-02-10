package com.ifanr.tangzhi.ui.signin.email

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.exceptions.PhoneNotBindException
import com.ifanr.tangzhi.ext.finishDelay
import com.ifanr.tangzhi.ext.toast
import com.ifanr.tangzhi.ext.viewModelOf
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.statusBar
import com.ifanr.tangzhi.ui.widgets.observeLoadingLiveData
import kotlinx.android.synthetic.main.activity_sign_in_by_email.*

/**
 * 邮箱登录
 */
@Route(path = Routes.signInByEmail)
class SignInByEmailActivity : BaseViewModelActivity() {

    companion object {
        private const val TAG = "SignInByEmailActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_by_email)
        statusBar(whiteText = false)

        toolBar.close.setOnClickListener { finish() }

        val vm: SignInByEmailViewModel = viewModel()
        observeLoadingLiveData(vm.loading)
        vm.event.observe(this, Observer {
            when (it) {

                Event.IncorrectEmail -> toast(R.string.sign_in_by_email_error)

                Event.IncorrectPwd -> toast(R.string.sign_in_by_email_pwd_hint)

                is Event.SignInError -> {
                    when (it.t) {
                        is PhoneNotBindException -> {
                            ARouter.getInstance().build(Routes.bindPhone)
                                .withString(Routes.bindPhoneType, "email")
                                .navigation(this)
                            finishDelay()
                        }

                        else -> {
                            toast(it.t.message ?: "")
                            Log.e(TAG, it.t.message, it.t)
                        }
                    }
                }

                Event.SignInSuccess -> finish()
            }
        })
        email.onTextChangedListener = { vm.email.value = it }
        pwd.onTextChangedListener = { vm.pwd.value = it }
        signIn.setOnClickListener { vm.signIn() }
    }
}
