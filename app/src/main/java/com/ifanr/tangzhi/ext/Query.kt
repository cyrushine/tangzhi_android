package com.ifanr.tangzhi.ext

import com.minapp.android.sdk.database.query.Query

/**
 * @param page from 0
 */
fun Query.setPageInfo(page: Int, pageSize: Int) =
    limit(pageSize).offset(page * pageSize)