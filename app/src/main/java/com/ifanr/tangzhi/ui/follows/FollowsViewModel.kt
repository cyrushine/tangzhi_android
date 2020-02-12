package com.ifanr.tangzhi.ui.follows

import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import javax.inject.Inject

class FollowsViewModel @Inject constructor(
    private val repository: BaasRepository
): BaseViewModel() {

    fun loadList() = repository.followsList()

}