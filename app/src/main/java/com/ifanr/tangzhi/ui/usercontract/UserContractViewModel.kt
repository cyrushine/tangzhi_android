package com.ifanr.tangzhi.ui.usercontract

import androidx.lifecycle.MutableLiveData
import com.ifanr.tangzhi.ext.ioTask
import com.ifanr.tangzhi.model.BaasContent
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.util.LoadingState
import javax.inject.Inject

class UserContractViewModel @Inject constructor (
    private val repository: BaasRepository
): BaseViewModel() {

    val contract = MutableLiveData<BaasContent>()
    val loading = MutableLiveData<LoadingState>()
    val toast = MutableLiveData<String>()

    init {
        repository.getContentById("1576475282813162")
            .ioTask(vm = this, loadingState = loading, loadingDelay = false)
            .subscribe({
                contract.value = it
            }, {
                toast.value = "用户协议加载异常(${it.message})"
            })
    }

}