package com.ifanr.android.tangzhi.ext

import com.google.gson.JsonObject

fun JsonObject.getAsString(key: String) = try { get(key).asString } catch (e: Exception) { null }

fun JsonObject.getAsFloat(key: String) = try { get(key).asFloat } catch (e: Exception) { null }

fun JsonObject.getAsBoolean(key: String) = try { get(key).asBoolean } catch (e: Exception) { null }

fun JsonObject.getAsStringArray(key: String) =
    try { get(key).asJsonArray.map { it.asString } } catch (e: Exception) { null }