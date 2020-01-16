package com.ifanr.tangzhi.exceptions

import java.lang.Exception

/**
 * 破坏了唯一性约束
 */
class UniqueRuleException: Exception {

    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)

}