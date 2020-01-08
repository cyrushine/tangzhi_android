package com.ifanr.tangzhi.ui.index.profile

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ifanr.tangzhi.Event
import com.ifanr.tangzhi.EventBus
import com.ifanr.tangzhi.model.UserProfile
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.Subject
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val repository: BaasRepository,
    private val bus: EventBus
) : BaseViewModel() {

    companion object {
        private const val TAG = "ProfileViewModel"
    }

    val profile = MutableLiveData<UserProfile>()

    // 背景图
    val banner = MediatorLiveData<String>()

    init {
        banner.addSource(profile) {
            val selected = it?.banner
            if (!selected.isNullOrEmpty()) {
                banner.value = selected
            } else {
                repository.cachedUserBannerList()
                    .subscribeOn(Schedulers.io())
                    .autoDispose(this)
                    .subscribe(Consumer {
                        if (!it.isNullOrEmpty()) {
                            banner.postValue(it.random())
                        }
                    })
            }
        }

        bus.subscribe(this, Consumer {
            when (it) {
                Event.SignIn -> Log.d(TAG, "SignIn")
                Event.SignOut -> Log.d(TAG, "SignOut")
            }
        })

        loadProfile()
    }

    fun syncLoginState() {
        if (profile.value == null && repository.signedIn()) {
            loadProfile()
        }
    }

    fun signOut() {
        bus.post(Event.SignOut)
    }

    private fun loadProfile() {
        repository.loadUserProfile()
            .subscribeOn(Schedulers.io())
            .autoDispose(this)
            .subscribe(Consumer { profile.postValue(it) })
    }
}
