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
    private val observers = CopyOnWriteArraySet<CompletableObserver>()

    init {
        initQuery.invoke(query)
        addInvalidatedCallback {
            observers.forEach { it.onComplete() }
            observers.clear()
        }
    }

    @SuppressLint("CheckResult")
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, T>
    ) {
        query.returnTotalCount(true)
        query.offset(0)
        query.limit(params.requestedLoadSize)

        itemList.query(query, clz)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(this)
            .subscribe(Consumer {
                total = it.totalCount?.toInt() ?: 0
                val list = it.objects ?: emptyList()
                val nextPage = if (total > list.size) list.size else null
                callback.onResult(list, 0, total, null, nextPage)
            })
    }

    @SuppressLint("CheckResult")
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        query.returnTotalCount(false)
        query.offset(params.key)
        query.limit(params.requestedLoadSize)

        itemList.query(query, clz)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(this)
            .subscribe(Consumer {
                val list = it.objects ?: emptyList()
                val nextPageOffset = params.key + list.size
                callback.onResult(list, if (total > nextPageOffset) nextPageOffset else null)
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        callback.onResult(emptyList(), null)
    }

    class DataSourceScopeCompletable (
        private val dataSource: BaseDataSource<*>
    ): Completable() {
        override fun subscribeActual(observer: CompletableObserver) {
            dataSource.observers.add(observer)
        }
    }
}

fun <T> Single<T>.autoDispose(dataSource: BaseDataSource<*>) =
    autoDispose(BaseDataSource.DataSourceScopeCompletable(dataSource))