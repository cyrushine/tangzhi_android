package com.ifanr.tangzhi.exceptions

/**
 * 邮箱登录失败
 */
class SignInByEmailFailException: Exception {

    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)

}