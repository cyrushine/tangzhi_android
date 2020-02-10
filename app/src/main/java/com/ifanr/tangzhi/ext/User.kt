package com.ifanr.tangzhi.ext

import com.minapp.android.sdk.user.User

val User.wechatName: String?
    get() {
        return runCatching {
            getJsonObject("_provider")?.getAsJsonObject("oauth_wechat_native")
                ?.get("nickname")?.asString
        }.getOrNull()
    }