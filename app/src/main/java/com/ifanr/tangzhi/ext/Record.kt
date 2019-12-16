package com.ifanr.tangzhi.ext

import com.google.gson.JsonObject
import com.minapp.android.sdk.database.Record
import java.lang.reflect.Constructor


private val CONSTRUCTS = mutableMapOf<Class<*>, Constructor<*>>()

@Throws(Exception::class)
private fun findSuitableConstruct(clz: Class<*>): Constructor<*> {
    synchronized(CONSTRUCTS) {
        return CONSTRUCTS.getOrPut(clz) {
            clz.getConstructor(JsonObject::class.java)
        }
    }
}

fun Record.getSafeString(prop: String): String =
    getString(prop) ?: ""

/**
 * prop is a json string, serialize to object
 */
fun <T> Record.getSafeArray(prop: String, clz: Class<T>): List<T> =
    getArray(prop, clz) ?: emptyList()

fun <T> Record.getSafeArrayByConstruct(prop: String, clz: Class<T>): List<T> {
    return try {
        getSafeArray(prop).map { findSuitableConstruct(clz).newInstance(it) as T }
    } catch (e: Exception) { emptyList() }
}

fun Record.getSafeArray(prop: String): List<JsonObject> =
    getSafeArray(prop, JsonObject::class.java)

fun Record.getSafeStringArray(prop: String): List<String> =
    getSafeArray(prop, String::class.java)

fun Record.getSafeLong(prop: String): Long =
    getLong(prop) ?: 0L

fun Record.getSafeFloat(prop: String): Float =
    getFloat(prop) ?: 0f

fun Record.getSafeBoolean(prop: String): Boolean =
    getBoolean(prop) ?: false

fun Record.getSafeId(): String = id ?: ""