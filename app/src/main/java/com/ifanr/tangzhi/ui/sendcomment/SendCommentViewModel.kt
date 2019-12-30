package com.ifanr.tangzhi.ui.sendcomment

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.ifanr.tangzhi.ui.base.BaseViewModel
import io.reactivex.Completable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SendCommentViewModel @Inject constructor(): BaseViewModel() {

    val productName = MutableLiveData<String>()

    val productId = MutableLiveData<String>()

    val parentId = MutableLiveData<String>()

    val content = MutableLiveData<String>()

    val sendButtonEnable = MediatorLiveData<Boolean>().apply { value = false }

    init {
        sendButtonEnable.addSource(content) {
            sendButtonEnable.value = !it.isNullOrEmpty()
        }
    }

    /**
     * 发表评论
     */
    fun sendComment(): Completable {
        return Completable.timer(1000, TimeUnit.MILLISECONDS)
    }

}