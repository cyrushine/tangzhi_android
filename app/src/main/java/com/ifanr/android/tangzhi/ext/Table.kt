package com.ifanr.android.tangzhi.ext

import com.ifanr.android.tangzhi.Const
import com.ifanr.android.tangzhi.exceptions.NotFoundException
import com.minapp.android.sdk.database.Record
import com.minapp.android.sdk.database.Table
import com.minapp.android.sdk.database.query.Query
import com.minapp.android.sdk.database.query.Where
import com.minapp.android.sdk.util.PagedList
import io.reactivex.Single
import java.lang.reflect.Constructor


private val CONSTRUCTORS: MutableMap<Class<*>, Constructor<*>> = hashMapOf()

@Suppress("UNCHECKED_CAST")
private fun <T> findExplicitConstructor(clz: Class<T>): Constructor<T> {
    return synchronized(CONSTRUCTORS) {
        CONSTRUCTORS.getOrPut(clz) { clz.getDeclaredConstructor(Record::class.java) } as? Constructor<T>
            ?: throw IllegalStateException("explicit constructor of ${clz.canonicalName} not found")
    }
}


fun <T> Table.getById(id: String, clz: Class<T>): Single<T> = Single.fromCallable {
    query(Query().put(Where().equalTo(Record.ID, id))).objects?.firstOrNull()
        ?.let { findExplicitConstructor(clz).newInstance(it) } ?: throw NotFoundException()
}

fun <T> Table.query(
    where: Where,
    page: Int = 0,
    pageSize: Int = Const.PAGE_SIZE,
    clz: Class<T>
): Single<PagedList<T>> {
    return Single.fromCallable {
        query(Query().put(where).setPageInfo(page, pageSize).returnTotalCount(page == 0))
            .transform { findExplicitConstructor(clz).newInstance(it) }
    }
}