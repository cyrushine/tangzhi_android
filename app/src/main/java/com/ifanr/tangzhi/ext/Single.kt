package com.ifanr.tangzhi.ext

import androidx.lifecycle.MutableLiveData
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import com.ifanr.tangzhi.util.LoadingState
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Single<T>.networkJob(vm: BaseViewModel, loading: MutableLiveData<LoadingState>? = null) =
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { loading?.value = LoadingState.SHOW_DELAY }
        .doAfterTerminate { loading?.value = LoadingState.DISMISS }
        .autoDispose(vm)