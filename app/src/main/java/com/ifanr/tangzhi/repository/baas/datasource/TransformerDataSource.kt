package com.ifanr.tangzhi.repository.baas.datasource

import androidx.annotation.WorkerThread
import androidx.paging.PageKeyedDataSource
import com.ifanr.tangzhi.ext.queryByOffset
import com.minapp.android.sdk.database.Table
import com.minapp.android.sdk.database.query.Query

abstract class TransformerDataSource<T, K> (
    private val table: Table,
    private val clz: Class<T>,
    initQuery: Query.() -> Unit = {}
) : PageKeyedDataSource<Int, K>() {

    companion object {
        private const val TAG = "TransformerDataSource"
    }

    private val query = Query()
    private var total = 0

    var endOfList = false
        private set

    init {
        initQuery.invoke(query)
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, K>
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
        if (nextOffset == null) endOfList = true
        callback.onResult(doTransform(list), 0, total, null, nextOffset)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, K>) {
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
        if (nextOffset == null) endOfList = true
        callback.onResult(doTransform(list), nextOffset)
    }


    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, K>) {
        callback.onResult(emptyList(), null)
    }

    private fun doTransform(source: List<T>): List<K> {
        val dest = transform(source)
        assert(source.size == dest.size) {
            "size of return should be equal to param list's size" }
        return dest
    }

    /**
     * @return size of return should be equal to param list's size
     */
    abstract fun transform(source: List<T>): List<K>
}