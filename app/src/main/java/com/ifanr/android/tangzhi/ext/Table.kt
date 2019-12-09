package com.ifanr.android.tangzhi.ext

import com.ifanr.android.tangzhi.exceptions.NotFoundException
import com.minapp.android.sdk.database.Record
import com.minapp.android.sdk.database.Table
import com.minapp.android.sdk.database.query.Query
import com.minapp.android.sdk.database.query.Where
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