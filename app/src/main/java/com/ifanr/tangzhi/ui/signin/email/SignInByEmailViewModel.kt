package com.ifanr.tangzhi.ui.signin.email

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ifanr.tangzhi.ext.networkJob
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.repository.ifanr.IfanrServerRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.util.LoadingState
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import javax.inject.Inject

class SignInByEmailViewModel @Inject constructor(
    private val repository: BaasRepository,
    private val ifanrServer: IfanrServerRepository
): BaseViewModel() {

    companion object {
        private const val TAG = "SignInByEmailViewModel"
    }

    val email = MutableLiveData<String>()
    val pwd = MutableLiveData<String>()
    val loading = MutableLiveData<LoadingState>()
    val event = MutableLiveData<Event>()

    fun signIn() {
        val e = email.value?.trim()
        val p = pwd.value?.trim()
        Log.d(TAG, "sign in by $e, $p")

        if (e.isNullOrEmpty() || !e.matches(Regex(".+@.+\\..+"))) {
            event.value = Event.IncorrectEmail
            return
        }
        if (p.isNullOrEmpty()) {
            event.value = Event.IncorrectPwd
            return
        }

        ifanrServer.signInByEmail(e, p)
            .networkJob(vm = this, loadingState = loading, loadingDelay = false)
            .subscribe({ event.value = Event.SignInSuccess }, {
                event.value = Event.SignInError(t = it)
            })
    }

}

sealed class Event {
    object SignInSuccess : Event()
    data class SignInError(val t: Throwable): Event()
    object IncorrectEmail: Event()
    object IncorrectPwd: Event()
}