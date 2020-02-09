package com.ifanr.tangzhi.appmgr

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.ifanr.tangzhi.ext.getSafeString
import com.ifanr.tangzhi.util.AppGson
import io.reactivex.Single
import java.nio.charset.Charset
import javax.inject.Inject

class AppMgrImpl @Inject constructor(
    private val telephonyManager: TelephonyManager,
    private val assetManager: AssetManager
): AppMgr {

    /**
     * 国家英文简称（CN）-> 手机国家代码（86）
     */
    private var phoneCodes = mapOf<String, String>()


    @SuppressLint("MissingPermission")
    override fun getPhoneNumber(): Single<String> = Single.fromCallable {
        var countryCode: String? = null
        try {
            countryCode = telephonyManager.simCountryIso
            if (countryCode.isNullOrEmpty())
                countryCode = telephonyManager.networkCountryIso
        } catch (e: Exception) {}
        val phoneCode = loadPhoneCodes().blockingGet()[countryCode]

        var phone = telephonyManager.line1Number
        if (phone.startsWith("+"))
            phone = phone.substring(1)
        if (!phoneCode.isNullOrEmpty() && phone.startsWith(phoneCode))
            phone = phone.replaceFirst(phoneCode, "")
        phone ?: ""
    }

    /**
     * 加载手机国家码列表
     */
    private fun loadPhoneCodes() = Single.fromCallable {
        if (phoneCodes.isEmpty()) {
            synchronized(this) {
                if (phoneCodes.isEmpty()) {
                    phoneCodes = AppGson.fromJson(
                        assetManager.open("country_phone_code.json").use { String(it.readBytes()) }, JsonArray::class.java)
                        .flatMap { (it as JsonObject).getAsJsonArray("data").map { it as JsonObject } }
                        .map { it.getSafeString("countryCode") to it.getSafeString("phoneCode") }
                        .toMap()
                }
            }
        }
        phoneCodes
    }
}

