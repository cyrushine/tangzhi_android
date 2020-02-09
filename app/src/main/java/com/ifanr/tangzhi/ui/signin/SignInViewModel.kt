package com.ifanr.tangzhi.ui.signin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.Event
import com.ifanr.tangzhi.EventBus
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.toast
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.runInIO
import com.ifanr.tangzhi.ui.widgets.dismissLoading
import com.ifanr.tangzhi.util.LoadingState
import com.minapp.android.sdk.auth.Auth
import com.minapp.android.sdk.database.Table
import com.minapp.android.sdk.database.query.Query
import com.minapp.android.sdk.database.query.Where
import com.minapp.android.sdk.user.User
import com.minapp.android.sdk.wechat.WechatComponent
import com.minapp.android.sdk.wechat.WechatSignInCallback
import java.lang.Exception
import javax.inject.Inject

class SignInViewModel @Inject constructor(
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
                bus.post(Event.SignIn)
                loading.postValue(LoadingState.DISMISS)
                event.postValue(SignInEvent.SignInSuccess)
            }
        }

        override fun onFailure(ex: Exception?) {
            loading.value = LoadingState.DISMISS
            Log.e(TAG, ex?.message, ex)
            event.value = SignInEvent.SignInFail(ex)
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
}