package com.ifanr.tangzhi.ui.follows

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.ifanr.tangzhi.Event
import com.ifanr.tangzhi.EventBus
import com.ifanr.tangzhi.ext.ioTask
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import io.reactivex.functions.Consumer
import javax.inject.Inject

class FollowsViewModel @Inject constructor(
    private val repository: BaasRepository,
    private val bus: EventBus
): BaseViewModel() {

    val list = MutableLiveData<PagedList<Product>>()
    val toast = MutableLiveData<String>()

    init {
        loadList()
        bus.subscribe(this, Consumer {
            when (it) {
                is Event.FollowEvent -> loadList()
            }
        })
    }

    private fun loadList() {
        repository.followsList()
            .ioTask(vm = this)
            .subscribe({
                list.value = it
            }, {
                toast.value = it.message
            })
    }

}