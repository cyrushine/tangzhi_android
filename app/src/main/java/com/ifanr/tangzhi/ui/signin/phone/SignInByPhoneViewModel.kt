package com.ifanr.tangzhi.ui.signin.phone

import androidx.lifecycle.MutableLiveData
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.ioTask
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.util.LoadingState
import javax.inject.Inject

class SignInByPhoneViewModel @Inject constructor(
    private val repository: BaasRepository
): BaseViewModel() {

    companion object {
        private const val TAG = "SignInByPhoneViewModel"
    }

    val phone = MutableLiveData<String>()
    val countryCode = MutableLiveData<Int>()
    val smsCode = MutableLiveData<String>()
    val loading = MutableLiveData<LoadingState>()
    val toast = MutableLiveData<Pair<Int, String>>()
    val signInResult = MutableLiveData<Boolean>()
    val event = MutableLiveData<Event>()

    fun sendSmsCode() {
        val num = phone.value
        if (num.isNullOrEmpty()) {
            toast.value = R.string.sign_in_by_phone_number_hint to ""
            return
        }

        repository.sendSmsCode(num)
            .ioTask(vm = this, loadingState = loading)
            .subscribe({
                toast.value = R.string.sign_in_by_phone_sms_code_sended to ""
                event.value = Event.SmsCodeSended
            }, {
                toast.value = 0 to (it.message ?: "")
            })
    }

    fun signIn() {
        val phoneNum = phone.value
        val smsCode = smsCode.value

        if (phoneNum.isNullOrEmpty()) {
            toast.value = R.string.sign_in_by_phone_number_hint to ""
            return
        }

        if (smsCode.isNullOrEmpty()) {
            toast.value = R.string.sign_in_by_phone_sms_code_hint to ""
            return
        }

        repository.signInByPhone(phoneNum, smsCode)
            .ioTask(vm = this, loadingState = loading, loadingDelay = false)
            .subscribe({
                signInResult.value = true
            }, {
                signInResult.value = false
                toast.value = 0 to (it.message ?: "")
            })
    }

    sealed class Event {
        object SmsCodeSended: Event()
    }
}