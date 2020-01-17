package com.ifanr.tangzhi.ui.widgets

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ui.base.autoDispose
import com.ifanr.tangzhi.ui.base.dialog.BaseDialog
import com.ifanr.tangzhi.ui.base.dialog.BaseDialogFragment
import com.ifanr.tangzhi.ui.base.dialog.autoDispose
import com.ifanr.tangzhi.ui.setStatusTextColor
import com.ifanr.tangzhi.util.LoadingState
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.SchedulerSupport
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.app_dialog_loading.*
import java.util.concurrent.TimeUnit


private const val TAG = "tangzhi_app_loading"

fun AppCompatActivity.showLoading(delay: Boolean = false) {
    if (supportFragmentManager.findFragmentByTag(TAG) == null) {
        LoadingDialogFragment.show(supportFragmentManager, delay = delay, tag = TAG)
    }
}

fun AppCompatActivity.dismissLoading() {
    (supportFragmentManager.findFragmentByTag(TAG) as? LoadingDialogFragment)?.dismiss()
}

fun AppCompatActivity.observeLoadingLiveData(liveData: LiveData<LoadingState>) {
    liveData.observe(this, Observer {
        when (it) {
            LoadingState.DISMISS -> dismissLoading()
            LoadingState.SHOW_DELAY -> showLoading(delay = true)
            LoadingState.SHOW -> showLoading()
        }
    })
}

fun Fragment.showLoading(delay: Boolean = false) {
    if (childFragmentManager.findFragmentByTag(TAG) == null) {
        LoadingDialogFragment.show(childFragmentManager, delay = delay, tag = TAG)
    }
}

fun Fragment.dismissLoading() {
    (childFragmentManager.findFragmentByTag(TAG) as? LoadingDialogFragment)?.dismiss()
}

fun Fragment.observeLoadingLiveData(liveData: LiveData<LoadingState>) {
    liveData.observe(this, Observer {
        when (it) {
            LoadingState.DISMISS -> dismissLoading()
            LoadingState.SHOW_DELAY -> showLoading(delay = true)
            LoadingState.SHOW -> showLoading()
        }
    })
}

@SchedulerSupport("AndroidSchedulers.mainThread()")
fun <T> Single<T>.bindLoading(liveData: MutableLiveData<LoadingState>): Single<T> =
    doOnSubscribe { liveData.value = LoadingState.SHOW }
        .doAfterTerminate { liveData.value = LoadingState.DISMISS }


/**
 * 通用的 loading
 */
class LoadingDialogFragment: DialogFragment() {

    private var delay = false

    companion object {

        private const val ARG_DELAY = "ARG_DELAY"

        fun show(fm: FragmentManager, delay: Boolean = false, tag: String? = null) {
            LoadingDialogFragment().apply {
                arguments = bundleOf(ARG_DELAY to delay)
                this.show(fm, tag)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delay = arguments?.getBoolean(ARG_DELAY, delay) ?: delay
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return LoadingDialog(requireContext(), delay)
    }
}

class LoadingDialog (
    context: Context,
    private val delay: Boolean = false
) : BaseDialog(context) {

    companion object {
        private const val DELAY = 1000L
    }

    init {
        setCanceledOnTouchOutside(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_dialog_loading)
        if (delay) {
            loadingRoot.visibility = View.GONE
            Single.timer(DELAY, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .autoDispose(this)
                .subscribe(Consumer {
                    loadingRoot.visibility = View.VISIBLE
                })
        }
    }

    override fun show() {
        super.show()
        window?.also { w ->
            w.setDimAmount(0f)
        }
    }
}