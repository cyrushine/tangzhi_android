package com.ifanr.tangzhi.ext

import com.minapp.android.sdk.auth.Auth

fun userId(): String? =
    if (Auth.signedIn() && !Auth.isAnonymous())
        Auth.currentUserWithoutData()?.userId?.toString()
    else
        null