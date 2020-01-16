package com.ifanr.tangzhi.ui.sendcomment

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import com.ifanr.tangzhi.util.LoadingState
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SendCommentViewModel @Inject constructor(
    private val repository: BaasRepository
): BaseViewModel() {

    companion object {
        private const val TAG = "SendCommentViewModel"
    }

    private var productId = ""
    private var rootId = ""
    private var parentId: String? = null
    private var replyId: String? = null
    private var replyTo: Long? = null
    val content = MediatorLiveData<String>()
    val productName = MutableLiveData<String>()
    val loading = MutableLiveData<LoadingState>()
    val toast = MutableLiveData<String>()
    val close = MutableLiveData<Boolean>()

    val sendButtonEnable = MediatorLiveData<Boolean>()
        .apply { value = false }

    init {
        sendButtonEnable.addSource(content) {
            sendButtonEnable.value = !it.isNullOrEmpty()
        }
    }

    fun init(productId: String, rootId: String, parentId: String?, replyId: String?, replyTo: Long?) {
        this.productId = productId
        this.rootId = rootId
        this.parentId = parentId
        this.replyId = replyId
        this.replyTo = replyTo
        Log.d(TAG, "productId: $productId, rootId: $rootId, parentId: $parentId, " +
                "replyId: $replyId, replyTo: $replyTo")
    }

    /**
     * 发表评论
     */
    fun sendComment() {
        if (sendButtonEnable.value == true) {
            val comment = content.value
            if (!comment.isNullOrEmpty()) {
                repository.sendComment(productId, rootId, comment, parentId, replyId, replyTo)
                    .subscribeOn(Schedulers.io())
                    .delay(1000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { loading.value = LoadingState.SHOW }
                    .doAfterTerminate { loading.value = LoadingState.DISMISS }
                    .autoDispose(this)
                    .subscribe({
                        toast.value = "发表成功"
                        closeDelay()
                    }, {
                        toast.value = it.message
                    })
            }
        }
    }

    private fun closeDelay() {
        Completable.timer(250, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .autoDispose(this)
            .subscribe { close.postValue(true) }
    }

}