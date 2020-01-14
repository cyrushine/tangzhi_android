package com.ifanr.tangzhi.ui.base.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import androidx.annotation.FloatRange
import androidx.appcompat.app.AppCompatDialog
import com.ifanr.tangzhi.ext.dp2px
import com.uber.autodispose.autoDispose
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.roundToInt

abstract class BaseDialog: AppCompatDialog {

    val observers = CopyOnWriteArrayList<CompletableObserver>()

    // 窗口高度的占比（对于屏幕）
    @FloatRange(from = 0.0, to = 1.0)
    protected var heightPercent = 0.7f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, theme: Int) : super(context, theme)
    constructor(
        context: Context?,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener)

    override fun show() {
        super.show()

        window?.also { w ->
            w.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val dm = context.resources.displayMetrics
            val lp = w.attributes.apply {
                width = dm.widthPixels - context.dp2px(25) * 2
                height = dm.heightPixels.times(heightPercent).roundToInt()
                gravity = Gravity.CENTER
            }
            w.attributes = lp
        }
    }

    override fun onStop() {
        super.onStop()
        observers.forEach { it.onComplete() }
        observers.clear()
    }
}

class BaseDialogScopeCompletable (
    private val dialog: BaseDialog
): Completable() {
    override fun subscribeActual(observer: CompletableObserver) {
        dialog.observers.add(observer)
    }
}

fun <T> Single<T>.autoDispose(dialog: BaseDialog) =
    autoDispose(BaseDialogScopeCompletable(dialog))

