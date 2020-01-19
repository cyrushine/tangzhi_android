package com.ifanr.tangzhi.ext

import androidx.lifecycle.MutableLiveData
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import com.ifanr.tangzhi.util.LoadingState
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun Completable.networkJob(
    vm: BaseViewModel,
    loadingState: MutableLiveData<LoadingState>? = null,
    loadingDelay: Boolean = true
) = subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .doOnSubscribe {
        if (loadingState != null)
            loadingState.value = if (loadingDelay) LoadingState.SHOW_DELAY else LoadingState.SHOW
    }
    .doAfterTerminate {
        if (loadingState != null)
            loadingState.value = LoadingState.DISMISS
    }
    .autoDispose(vm)