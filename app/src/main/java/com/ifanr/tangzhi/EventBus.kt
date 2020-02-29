package com.ifanr.tangzhi

import android.os.Looper
import androidx.annotation.AnyThread
import com.ifanr.tangzhi.model.Comment
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject
import javax.inject.Singleton

class EventBus {

    private val subject: Subject<Event> = PublishSubject.create()

    @AnyThread
    fun post(event: Event) {
        if (Looper.myLooper() == Looper.getMainLooper())
            subject.onNext(event)
        else
            mainHandler.post { subject.onNext(event) }
    }

    fun subscribe(
        vm: BaseViewModel,
        onNext: Consumer<Event>,
        observeOn: Scheduler = AndroidSchedulers.mainThread()) {
        subject.observeOn(observeOn).autoDispose(vm).subscribe(onNext)
    }

    fun subscribe(
        observeOn: Scheduler = Schedulers.io(),
        onNext: Consumer<Event>) =
        subject.observeOn(observeOn).subscribe(onNext)

}

sealed class Event {
    data class SignIn(val type: Type): Event() {
        enum class Type {
            WECHAT, EMAIL, PHONE
        }
    }
    object SignOut: Event()
    object ProfileChanged: Event()
    data class ReviewCreated(val review: Comment): Event()
    data class ReviewChanged(val review: Comment): Event()
    data class CommentCreated(val comment: Comment): Event()

    // 关注产品/取消关注 事件
    data class FollowEvent(
        val productId: String,  // 产品 id
        val follow: Boolean     // true - 关注事件，false - 取消关注
    ): Event()
}
