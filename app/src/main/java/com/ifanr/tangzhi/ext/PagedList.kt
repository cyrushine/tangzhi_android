package com.ifanr.tangzhi.ext

import androidx.paging.DataSource
import androidx.paging.PagedList
import com.ifanr.tangzhi.Const
import com.ifanr.tangzhi.mainThreadExecutor
import com.ifanr.tangzhi.workerThreadExecutor
import java.util.concurrent.Executor

fun <Key, Value> pagedList (
    dataSource: DataSource<Key, Value>,
    config: PagedList.Config = Const.pagedListConfig,
    notifyExecutor: Executor = workerThreadExecutor,
    fetchExecutor: Executor = workerThreadExecutor,
    boundaryCallback: PagedList.BoundaryCallback<Value>? = null,
    initialKey: Key? = null
) = PagedList (
    dataSource,
    config,
    notifyExecutor,
    fetchExecutor,
    boundaryCallback,
    initialKey
)