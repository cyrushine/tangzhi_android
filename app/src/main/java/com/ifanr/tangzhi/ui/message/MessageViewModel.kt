package com.ifanr.tangzhi.ui.message

import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import javax.inject.Inject

class MessageViewModel @Inject constructor(
    private val repository: BaasRepository
): BaseViewModel() {
}