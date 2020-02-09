package com.ifanr.tangzhi.repository.ifanr

import com.ifanr.tangzhi.repository.ifanr.model.SignInByEmailRequest
import com.ifanr.tangzhi.repository.ifanr.model.SignInByEmailResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface IfanrServer {

    /**
     * 邮箱登录
     */
    @POST("api/v1/tangzhi-email-login/")
    fun signInByEmail(@Body req: SignInByEmailRequest): Single<SignInByEmailResponse>

}