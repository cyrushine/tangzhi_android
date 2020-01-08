package com.ifanr.tangzhi

import android.os.Looper
import androidx.annotation.AnyThread
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

class EventBus @Inject constructor() {

    private val subject: Subject<Event> = PublishSubject.create()

    @AnyThread
    fun post(event: Event) {
        if (Looper.myLooper() == Looper.getMainLooper())
            subject.onNext(event)
        else
            mainHandler.post { subject.onNext(event) }
    }

    fun subscribe(vm: BaseViewModel, onNext: Consumer<Event>) {
        subject.autoDispose(vm).subscribe(onNext)
    }
}

sealed class Event {
    object SignIn: Event()
    object SignOut: Event()
}
