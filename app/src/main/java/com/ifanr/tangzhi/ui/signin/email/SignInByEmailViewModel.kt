package com.ifanr.tangzhi.ui.signin.email

import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import javax.inject.Inject

class SignInByEmailViewModel @Inject constructor(
    private val repository: BaasRepository
): BaseViewModel() {

}