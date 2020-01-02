package com.ifanr.tangzhi.ext

import androidx.paging.DataSource
import androidx.paging.PagedList
import com.ifanr.tangzhi.Const
import com.ifanr.tangzhi.worker
import com.ifanr.tangzhi.workers
import java.util.concurrent.Executor

fun <Key, Value> pagedList (
    dataSource: DataSource<Key, Value>,
    config: PagedList.Config = Const.pagedListConfig,

    /**
     * 如果是 epoxy 使用 PagedList，这里的通知线程必需是 PagedListEpoxyController 的 model building thread
     */
    notifyExecutor: Executor = worker,
    fetchExecutor: Executor = workers,
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