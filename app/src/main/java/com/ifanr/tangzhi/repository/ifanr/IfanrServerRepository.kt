package com.ifanr.tangzhi.repository.ifanr

import io.reactivex.Completable

interface IfanrServerRepository {

    /**
     * 邮箱登录
     */
    fun signInByEmail(email: String, pwd: String): Completable

}