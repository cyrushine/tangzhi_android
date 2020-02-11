package com.ifanr.tangzhi.ui.signin.wechat

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ifanr.tangzhi.Event
import com.ifanr.tangzhi.EventBus
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.runInIO
import com.ifanr.tangzhi.ui.signin.email.SignInByEmailViewModel
import com.ifanr.tangzhi.util.LoadingState
import com.minapp.android.sdk.wechat.WechatComponent
import com.minapp.android.sdk.wechat.WechatSignInCallback
import java.lang.Exception
import javax.inject.Inject

class SignInByWechatViewModel @Inject constructor(
    private val bus: EventBus,
    private val repository: BaasRepository
): BaseViewModel() {

    companion object {
        private const val TAG = "SignInViewModel"
    }

    val loading = MutableLiveData<LoadingState>()
    val event = MutableLiveData<SignInEvent>()

    private val callback = object : WechatSignInCallback {
        override fun onSuccess() {
            runInIO {
                val phoneExist = runCatching {
                    !repository.currentUser().blockingGet().userPhone.isEmpty()
                }.getOrDefault(true)

                loading.postValue(LoadingState.DISMISS)
                event.postValue(
                    if (phoneExist) SignInEvent.SignInSuccess else SignInEvent.PhoneNotBind)
            }
        }

        override fun onFailure(ex: Exception?) {
            loading.value = LoadingState.DISMISS
            Log.e(TAG, ex?.message, ex)
            event.value =
                SignInEvent.SignInFail(ex)
        }
    }

    fun signInByWechat() {
        loading.value = LoadingState.SHOW
        WechatComponent.signIn(callback)
    }
}

sealed class SignInEvent {
    data class SignInFail(val ex: Exception?): SignInEvent()
    object SignInSuccess: SignInEvent()
    object PhoneNotBind: SignInEvent()
}