package com.ifanr.tangzhi.repository.baas.impl

import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.ifanr.tangzhi.model.SearchKey
import com.ifanr.tangzhi.repository.baas.SettingRepository
import com.ifanr.tangzhi.repository.baas.Tables
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(): SettingRepository {

    private val cache: MutableMap<String, Any> = mutableMapOf()
    private val cacheLock = ReentrantLock(true)

    init {
        userBanners()
            .delay(5000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun hottestSearch(): Single<List<SearchKey>> =
        getValue("hot_search_key", object: TypeToken<List<SearchKey>>(){}.type)


    override fun hiddenProductParams(): Single<List<String>> =
        getValue("hidden_product_param", object: TypeToken<List<String>>(){}.type)


    override fun productParamMapping(): Single<Map<String, String>> {
        val request: Single<JsonObject> =
            getValue("product_param_name_mapping", JsonObject::class.java)
        return request.map { json ->
            HashMap<String, String>().apply {
                json.keySet().forEach { key ->
                    try {
                        put(key, json[key].asString)
                    } catch (e: Exception) {}
                }
            }
        }
    }

    override fun userBanners(): Single<List<String>> =
        getValue("user_banner", object: TypeToken<List<String>>(){}.type)

    @Suppress("UNCHECKED_CAST")
    private fun <T> getValue(key: String, type: Type): Single<T> = Single.fromCallable {
        synchronized(cacheLock) {
            val value = cache[key]
            if (value != null) {
                value as T
            } else {
                val value = Tables.setting.getValue<T>(key, type).blockingGet()
                    ?: throw Exception("$key not found in settings")
                cache.put(key, value)
                value
            }
        }
    }
}