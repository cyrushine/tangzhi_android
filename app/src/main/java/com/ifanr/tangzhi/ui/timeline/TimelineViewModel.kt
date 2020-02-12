package com.ifanr.tangzhi.ui.timeline

import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import javax.inject.Inject

class TimelineViewModel @Inject constructor(
    private val repository: BaasRepository
): BaseViewModel() {


    fun loadList() = repository.timelineList()

}