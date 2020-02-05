package com.ifanr.tangzhi.ui.signin.phone

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.observeToast
import com.ifanr.tangzhi.ext.toast
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.signin.widget.FormSmsCodeInput
import com.ifanr.tangzhi.ui.statusBar
import com.ifanr.tangzhi.ui.widgets.observeLoadingLiveData
import kotlinx.android.synthetic.main.activity_sign_in_by_phone.*

/**
 * 手机号码登录（注册）
 */
@Route(path = Routes.signInByPhone)
class SignInByPhoneActivity : BaseViewModelActivity() {

    @Autowired(name = Routes.signInByPhoneNumber, required = false)
    @JvmField
    var phone: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_by_phone)
        statusBar(whiteText = false)
        toolBar.close.setOnClickListener { finish() }

        val vm: SignInByPhoneViewModel = viewModel()
        observeLoadingLiveData(vm.loading)
        vm.signInResult.observe(this, Observer {
            if (it == true) {
                finish()
            }
        })
        vm.toast.observe(this, Observer { it?.also {
            if (it.first > 0)
                toast(it.first)

            if (it.second.isNotEmpty())
                toast(it.second)
        } })
        vm.sendSmsCodeCountDown.observe(this, Observer { it?.also {
            smsCode.delayValue = it
        } })
        codeSelector.onValueChanged = { vm.countryCode.value = it }
        codeSelector.isEnabled = false
        phoneInput.onTextChangedListener = { vm.phone.value = it }
        smsCode.listener = object : FormSmsCodeInput.Listener {
            override fun onTextChanged(text: String) {
                vm.smsCode.value = text
            }

            override fun onSendSmsCodeClick() {
                vm.sendSmsCode()
            }
        }
        signIn.setOnClickListener { vm.signIn() }
        smsCode.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_GO -> {
                    vm.signIn()
                    true
                }
            }
            false
        })
    }
}
