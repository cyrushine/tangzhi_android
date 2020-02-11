package com.ifanr.tangzhi.ui.signin.bindlocalphone

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ifanr.tangzhi.appmgr.AppMgr
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.runInIO
import com.ifanr.tangzhi.util.LoadingState
import javax.inject.Inject

class BindLocalPhoneViewModel @Inject constructor(
    private val repository: BaasRepository,
    private val appMgr: AppMgr
): BaseViewModel() {

    companion object {
        private const val TAG = "BindLocalPhoneViewModel"
    }

    val loading = MutableLiveData<LoadingState>()
    val phone = MutableLiveData<String>()
    val event = MutableLiveData<Event>()

    /**
     * 一键登录
     */
    fun signInQuick() {
        runInIO {
            loading.postValue(LoadingState.SHOW)
            try {
                val num = try {
                    appMgr.getPhoneNumber().blockingGet()
                } catch (e: Exception) {
                    event.postValue(Event.PhoneNotFound)
                    return@runInIO
                }
                if (num.isEmpty()) {
                    event.postValue(Event.PhoneNotFound)
                    return@runInIO
                }

                phone.postValue(num)
                repository.updateUserPhone(num).blockingAwait()
                event.postValue(Event.SignInSuccess)
            } catch (e: Exception) {
                Log.e(TAG, e.message, e)
                event.postValue(Event.SignInFail(ex = e))
            } finally {
                loading.postValue(LoadingState.DISMISS)
            }
        }
    }

}

sealed class Event {
    object PhoneNotFound: Event()
    object SignInSuccess: Event()
    data class SignInFail(val ex: Exception): Event()
}