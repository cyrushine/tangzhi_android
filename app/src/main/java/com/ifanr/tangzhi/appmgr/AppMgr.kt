package com.ifanr.tangzhi.appmgr

import io.reactivex.Single

interface AppMgr {

    /**
     * 获取本机号码（不包含国家代码）
     */
    fun getPhoneNumber(): Single<String>

}