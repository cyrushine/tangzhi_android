package com.ifanr.tangzhi.repository.ifanr.model

import com.google.gson.annotations.SerializedName

/**
 * {
 *  "status": "ok",
 *  "token": "rrqobkzcvzhjj4gxaxb38zv11az03vwx",
 *  "expires_in": 2592000,
 *  "user_id": 142356761457433,
 *  "message": "Authentication succeeded.",
 *  "user_info": {
 *      "avatar": "http://ifanr-cdn.b0.upaiyun.com/wp-content/uploads/2017/09/image-4.jpeg",
 *      "nickname": "johndeng"
 *  }
 * }
 */
class SignInByEmailResponse {

    companion object {
        const val STATUS_OK = "ok"
    }

    @SerializedName("status")
    var status: String? = null

    @SerializedName("token")
    var token: String? = null

    @SerializedName("expires_in")
    var expiresIn: Long? = null

    @SerializedName("user_id")
    var userId: Long? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("user_info")
    var userInfo: UserInfo? = null

    class UserInfo {

        @SerializedName("avatar")
        var avatar: String? = null

        @SerializedName("nickname")
        var nickname: String? = null

    }
}