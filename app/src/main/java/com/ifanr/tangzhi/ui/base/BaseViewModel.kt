package com.ifanr.tangzhi.ui.base

import androidx.lifecycle.ViewModel
import com.uber.autodispose.autoDispose
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.CopyOnWriteArrayList

abstract class BaseViewModel: ViewModel() {

    val observers = CopyOnWriteArrayList<CompletableObserver>()

    override fun onCleared() {
        super.onCleared()
        observers.forEach { it.onComplete() }
        observers.clear()
    }


}

private class ViewModelScopeCompletable (
    private val viewModel: BaseViewModel
): Completable() {
    override fun subscribeActual(observer: CompletableObserver) {
        viewModel.observers.add(observer)
    }
}


fun <T> Single<T>.autoDispose(vm: BaseViewModel) =
    autoDispose(ViewModelScopeCompletable(vm))

fun <T> Observable<T>.autoDispose(vm: BaseViewModel) =
    autoDispose(ViewModelScopeCompletable(vm))