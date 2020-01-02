package com.ifanr.tangzhi.repository.baas.datasource

import androidx.paging.PageKeyedDataSource
import com.ifanr.tangzhi.ext.queryByOffset
import com.minapp.android.sdk.database.Table
import com.minapp.android.sdk.database.query.Query

abstract class BaseDataSource<T> (
    private val table: Table,
    private val clz: Class<T>,
    initQuery: Query.() -> Unit = {}
): PageKeyedDataSource<Int, T>() {

    companion object {
        private const val TAG = "BaseDataSource"
    }

    private val query = Query()
    private var total = 0

    init {
        initQuery.invoke(query)
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, T>
    ) {
        query.returnTotalCount(true)
        val response = table.queryByOffset (
            clz = clz,
            offset = 0,
            limit = params.requestedLoadSize,
            query = query
        ).blockingGet()
        total = response.total

        val list = response.data
        val nextOffset = if (list.size == response.limit) response.limit else null
        callback.onResult(list, 0, total, null, nextOffset)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        query.returnTotalCount(false)
        val response = table.queryByOffset (
            clz = clz,
            offset = params.key,
            limit = params.requestedLoadSize,
            query = query
        ).blockingGet()

        val list = response.data
        val nextOffset =
            if (list.size == response.limit) response.offset + response.limit else null
        callback.onResult(list, nextOffset)
    }


    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        callback.onResult(emptyList(), null)
    }
}