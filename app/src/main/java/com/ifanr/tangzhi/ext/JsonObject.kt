package com.ifanr.tangzhi.ext

import com.google.gson.JsonObject
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

fun JsonObject.getSafeString(key: String) = try { get(key).asString ?: "" } catch (e: Exception) { "" }

fun JsonObject.getSafeFloat(key: String) = try { get(key).asFloat } catch (e: Exception) { 0f }

fun JsonObject.getSafeBoolean(key: String) = try { get(key).asBoolean } catch (e: Exception) { false }

fun JsonObject.getSafeStringArray(key: String) =
    try { get(key).asJsonArray.map { it.asString } } catch (e: Exception) { listOf<String>() }

fun <T> JsonObject.getSafeArrayByConstruct(prop: String, clz: Class<T>): List<T> {
    return try {
        getAsJsonArray(prop).map { findSuitableConstruct(clz).newInstance(it) as T }
    } catch (e: Exception) { emptyList() }
}