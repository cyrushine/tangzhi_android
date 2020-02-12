package com.ifanr.tangzhi.ui.message.fragment

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.ifanr.tangzhi.ext.ioTask
import com.ifanr.tangzhi.model.Message
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.util.LoadingState
import io.reactivex.functions.Consumer
import javax.inject.Inject

class MessagePageViewModel @Inject constructor(
    private val repository: BaasRepository
) : BaseViewModel() {

    val pagedList = MutableLiveData<PagedList<Message>>()
    val error = MutableLiveData<String>()

    fun load(system: Boolean) {
        val task =
            if (system) repository.systemMessageList() else repository.messageList()
        task.ioTask(vm = this)
            .subscribe({ pagedList.value = it }, { error.value = it.message })

    }
}
