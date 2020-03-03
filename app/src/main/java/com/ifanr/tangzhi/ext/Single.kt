package com.ifanr.tangzhi.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import com.ifanr.tangzhi.ui.widgets.dismissLoading
import com.ifanr.tangzhi.ui.widgets.showLoading
import com.ifanr.tangzhi.util.LoadingState
import com.uber.autodispose.android.lifecycle.autoDispose
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Single<T>.ioTask(
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

fun <T> Single<T>.ioTask(activity: AppCompatActivity) =
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { activity.showLoading() }
        .doAfterTerminate { activity.dismissLoading() }
        .autoDispose(activity)