package com.ifanr.tangzhi.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import com.ifanr.tangzhi.ui.widgets.dismissLoading
import com.ifanr.tangzhi.ui.widgets.showLoading
import com.ifanr.tangzhi.util.LoadingState
import com.uber.autodispose.android.lifecycle.autoDispose
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Single<T>.ioTask(vm: BaseViewModel, loading: MutableLiveData<LoadingState>? = null) =
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { loading?.value = LoadingState.SHOW_DELAY }
        .doAfterTerminate { loading?.value = LoadingState.DISMISS }
        .autoDispose(vm)

fun <T> Single<T>.ioTask(activity: AppCompatActivity) =
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { activity.showLoading() }
        .doAfterTerminate { activity.dismissLoading() }
        .autoDispose(activity)