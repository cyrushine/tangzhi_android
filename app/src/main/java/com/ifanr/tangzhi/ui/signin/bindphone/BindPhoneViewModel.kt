package com.ifanr.tangzhi.ui.signin.bindphone

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ifanr.tangzhi.ext.networkJob
import com.ifanr.tangzhi.ext.wechatName
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.util.LoadingState
import io.reactivex.Completable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import javax.inject.Inject

class BindPhoneViewModel @Inject constructor(
    private val repository: BaasRepository
): BaseViewModel() {

    companion object {
        private const val TAG = "BindPhoneViewModel"
    }

    val event = MutableLiveData<Event>()
    val phone = MutableLiveData<String>()
    val smsCode = MutableLiveData<String>()
    val loading = MutableLiveData<LoadingState>()

    val wechatName = object: MutableLiveData<String>() {
        override fun onActive() {
            repository.currentUser()
                .map {
                    Log.d(TAG, it.toString())
                    it.wechatName ?: ""
                }
                .networkJob(vm = this@BindPhoneViewModel)
                .subscribe({
                    this.value = it
                    Log.d(TAG, "wechat name: $it")
                }, {
                    Log.e(TAG, "get wechat name fail", it)
                    event.value = Event.WechatNameNotFound(it)
                })
        }
    }

    fun bindPhone() {
        val number = phone.value
        if (number.isNullOrEmpty()) {
            event.value = Event.InvalidPhone
            return
        }

        val code = smsCode.value
        if (code.isNullOrEmpty()) {
            event.value = Event.SmsCodeNotFound
            return
        }

        UpdateUserPhone(number, code)
            .networkJob(vm = this, loadingState = loading, loadingDelay = false)
            .subscribe({ event.value = Event.BindPhoneSuccess },
                { event.value = Event.BindPhoneFail(it) })
    }

    fun UpdateUserPhone(phone: String, code: String) = Completable.fromAction {
        repository.verifySmsCode(phone, code).blockingAwait()
        repository.updateUserPhone(phone).blockingAwait()
    }

    fun sendSmsCode() {
        val number = phone.value
        if (number.isNullOrEmpty()) {
            event.value = Event.InvalidPhone
            return
        }

        repository.sendSmsCode(number)
            .networkJob(vm = this, loadingState = loading)
            .subscribe({ event.value = Event.SmsCodeSended },
                { event.value = Event.SendSmsCodeFail(it) })
    }


    sealed class Event {
        data class WechatNameNotFound(val t: Throwable): Event()
        object InvalidPhone: Event()
        object SmsCodeNotFound: Event()
        object SmsCodeSended: Event()
        data class SendSmsCodeFail(val t: Throwable): Event()
        object BindPhoneSuccess: Event()
        data class BindPhoneFail(val t: Throwable): Event()
    }


}