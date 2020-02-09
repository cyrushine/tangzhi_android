package com.ifanr.tangzhi.repository.ifanr.model

import com.google.gson.annotations.SerializedName

class SignInByEmailRequest {

    @SerializedName("email")
    var email: String? = null

    @SerializedName("password")
    var password: String? = null

    constructor()

    constructor(email: String?, password: String?) {
        this.email = email
        this.password = password
    }


}