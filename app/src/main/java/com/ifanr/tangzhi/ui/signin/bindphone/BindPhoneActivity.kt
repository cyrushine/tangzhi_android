package com.ifanr.tangzhi.ui.signin.bindphone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.toast
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.signin.widget.FormSmsCodeInput
import com.ifanr.tangzhi.ui.statusBar
import com.ifanr.tangzhi.ui.widgets.PrivacyPolicySpan
import com.ifanr.tangzhi.ui.widgets.UserAgreementSpan
import kotlinx.android.synthetic.main.activity_bind_phone.*

/**
 * 绑定手机
 */
@Route(path = Routes.bindPhone)
class BindPhoneActivity : BaseViewModelActivity() {

    @Autowired(name = Routes.bindPhoneType)
    @JvmField
    var type: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bind_phone)
        statusBar(whiteText = false)

        val vm: BindPhoneViewModel = viewModel()
        vm.event.observe(this, Observer {
            when (it) {
                is BindPhoneViewModel.Event.WechatNameNotFound -> {
                    toast(R.string.bind_phone_wechat_name_not_found)
                }

                BindPhoneViewModel.Event.BindPhoneSuccess -> {
                    toast(R.string.bind_phone_success)
                    finish()
                }

                is BindPhoneViewModel.Event.BindPhoneFail -> {
                    toast("${getString(R.string.bind_phone_fail)}(${it.t.message})")
                }

                BindPhoneViewModel.Event.SmsCodeNotFound -> {
                    toast(R.string.sign_in_by_phone_sms_code_hint)
                    smsCodeInput.requestFocus()
                }

                BindPhoneViewModel.Event.InvalidPhone -> {
                    toast(R.string.bind_phone_number_hint)
                    phoneInput.requestFocus()
                }

                BindPhoneViewModel.Event.SmsCodeSended -> {
                    toast(R.string.sign_in_by_phone_sms_code_sended)
                    smsCodeInput.startCountDown()
                }

                is BindPhoneViewModel.Event.SendSmsCodeFail -> {
                    toast(it.t.message ?: "")
                }
            }
        })
        toolBar.close.setOnClickListener { finish() }
        policyTv.movementMethod = LinkMovementMethod()

        // 微信绑定手机样式
        if (type == "wechat") {
            toolBar.titleTv.setText(R.string.bind_phone_wechat_title)
            wechat.visibility = View.VISIBLE
            emailTip.visibility = View.GONE
            vm.wechatName.observe(this, Observer {
                wechatNameTv.text = it
            })
            goBtn.setText(R.string.bind_phone_wechat_go)
            policyTv.text = SpannableStringBuilder().apply {
                append(getText(R.string.bind_phone_wechat_policy))
                setSpan(UserAgreementSpan(this@BindPhoneActivity), 22, 30,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                setSpan(PrivacyPolicySpan(this@BindPhoneActivity), 31, 37,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        } else {

            // 邮箱绑定手机样式
            toolBar.titleTv.setText(R.string.bind_phone_email_title)
            wechat.visibility = View.GONE
            emailTip.visibility = View.VISIBLE
            goBtn.setText(R.string.bind_phone_email_go)
            policyTv.text = SpannableStringBuilder().apply {
                append(getText(R.string.bind_phone_email_policy))
                setSpan(UserAgreementSpan(this@BindPhoneActivity), 5, 13,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                setSpan(PrivacyPolicySpan(this@BindPhoneActivity), 14, 20,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }

        phoneInput.onTextChangedListener = { vm.phone.value = it }
        smsCodeInput.listener = object : FormSmsCodeInput.Listener {
            override fun onTextChanged(text: String) {
                vm.smsCode.value = text
            }

            override fun onSendSmsCodeClick() {
                vm.sendSmsCode()
            }
        }
        goBtn.setOnClickListener { vm.bindPhone() }

    }
}
