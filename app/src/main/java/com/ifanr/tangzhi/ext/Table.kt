package com.ifanr.tangzhi.ext

import android.util.Log
import com.google.android.material.tabs.TabLayout
import com.ifanr.tangzhi.Const
import com.ifanr.tangzhi.exceptions.NotFoundException
import com.ifanr.tangzhi.model.Page
import com.minapp.android.sdk.database.Record
import com.minapp.android.sdk.database.Table
import com.minapp.android.sdk.database.query.Query
import com.minapp.android.sdk.database.query.Where
import com.minapp.android.sdk.util.PagedList
import io.reactivex.Single
import java.lang.reflect.Constructor


private val CONSTRUCTORS: MutableMap<Class<*>, Constructor<*>> = hashMapOf()

@Suppress("UNCHECKED_CAST")
fun <T> findExplicitConstructor(clz: Class<T>): Constructor<T> {
    return synchronized(CONSTRUCTORS) {
        CONSTRUCTORS.getOrPut(clz) { clz.getDeclaredConstructor(Record::class.java) } as? Constructor<T>
            ?: throw IllegalStateException("explicit constructor of ${clz.canonicalName} not found")
    }
}


inline fun <reified T> Table.getById(id: String): Single<T> = Single.fromCallable {
    query(Query().put(Where().equalTo(Record.ID, id))).objects?.firstOrNull()
        ?.let { findExplicitConstructor(T::class.java).newInstance(it) } ?: throw NotFoundException()
}

inline fun <reified T> Table.getByIds(ids: List<String>): Single<List<T>> = Single.fromCallable {
    query(Query().put(Where().containedIn(Record.ID, ids))).objects
        ?.map { findExplicitConstructor(T::class.java).newInstance(it) } ?: emptyList()
}

fun <T> Table.query(
    clz: Class<T>,
    page: Int = 0,
    pageSize: Int = Const.PAGE_SIZE,
    where: Where? = null,
    query: Query? = null
): Single<Page<T>> = Single.fromCallable {
    val q = query ?: Query()
    if (where != null)
        q.put(where)
    if (page >= 0 && pageSize > 0) {
        q.limit(pageSize)
        q.offset(page * pageSize)
    }
    q.returnTotalCount(page == 0)
    this.query(q).transform { findExplicitConstructor(clz).newInstance(it) }.let {
        Page (
            total = it.totalCount?.toInt() ?: 0,
            page = page,
            pageSize = pageSize,
            data = it.objects ?: emptyList()
        )
    }
}

inline fun <reified T> Table.query (
    page: Int = 0,
    pageSize: Int = Const.PAGE_SIZE,
    where: Where? = null,
    query: Query? = null
): Single<Page<T>> = Single.fromCallable {
    val q = query ?: Query()
    if (where != null)
        q.put(where)
    if (page >= 0 && pageSize > 0) {
        q.limit(pageSize)
        q.offset(page * pageSize)
    }
    q.returnTotalCount(page == 0)
    this.query(q).transform { findExplicitConstructor(T::class.java).newInstance(it) }.let {
        Page (
            total = it.totalCount?.toInt() ?: 0,
            page = page,
            pageSize = pageSize,
            data = it.objects ?: emptyList()
        )
    }
}