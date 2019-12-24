package com.ifanr.tangzhi.ui.base

import android.annotation.SuppressLint
import androidx.paging.PageKeyedDataSource
import com.ifanr.tangzhi.ext.query
import com.ifanr.tangzhi.repository.itemList
import com.minapp.android.sdk.database.Table
import com.minapp.android.sdk.database.query.Query
import com.uber.autodispose.autoDispose
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.DisposableHandle
import java.util.concurrent.CopyOnWriteArraySet

abstract class BaseDataSource<T> (
    private val table: Table,
    private val clz: Class<T>,
    initQuery: Query.() -> Unit = {}
): PageKeyedDataSource<Int, T>() {

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
        query.offset(0)
        query.limit(params.requestedLoadSize)

        val response = itemList.query(clz, query = query).blockingGet()
        total = response.total
        val list = response.data
        val nextPage = if (total > list.size) list.size else null
        callback.onResult(list, 0, total, null, nextPage)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        query.returnTotalCount(false)
        query.offset(params.key)
        query.limit(params.requestedLoadSize)

        val response = itemList.query(clz, query = query).blockingGet()
        val list = response.data
        val nextPageOffset = params.key + list.size
        callback.onResult(list, if (total > nextPageOffset) nextPageOffset else null)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        callback.onResult(emptyList(), null)
    }
}