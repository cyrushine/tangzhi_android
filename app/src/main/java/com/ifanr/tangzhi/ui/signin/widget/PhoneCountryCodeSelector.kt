package com.ifanr.tangzhi.ui.signin.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.gson.JsonObject
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.*
import com.ifanr.tangzhi.util.AppGson
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.properties.Delegates

private var COUNTRY_CODES = listOf<CountryCode>()

/**
 * 手机号码里的国家代码选择器
 */
@SuppressLint("SetTextI18n")
class PhoneCountryCodeSelector: ConstraintLayout {

    var onValueChanged: (Int) -> Unit = {}
        set(value) {
            field = value
            value.invoke(selected.code)
        }

    private lateinit var countryCode: TextView
    private var selected: CountryCode by Delegates.observable(
        CountryCode(code = 0, country = "")
    ) { _, _, new ->
        countryCode.text = "+${new.code} (${new.country})"
        onValueChanged.invoke(new.code)
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        inflateInto(R.layout.phone_country_code_selector)
        countryCode = findViewById(R.id.selectorCountryCodeTv)
        selected = CountryCode(
            code = 86,
            country = "中国大陆"
        )
        initCountryCodes()
        setOnClickListener {
            if (isEnabled) {
                AlertDialog.Builder(context)
                    .setCancelable(true)
                    .setItems(COUNTRY_CODES.map { "+${it.code} (${it.country})" }.toTypedArray()) { dialog, which ->
                        selected = COUNTRY_CODES[which]
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun initCountryCodes() {
        if (COUNTRY_CODES.isEmpty()) {
            LoadCountryCodeJob(context.assets)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { COUNTRY_CODES = it },
                    { Log.e("LoadCountryCodeJob", it.message, it) })
        }
    }
}


private fun LoadCountryCodeJob(am: AssetManager) = Single.fromCallable {
    val root = AppGson.fromJson(
        am.open("phone_code.json").use { String(it.readBytes()) }, JsonObject::class.java)
    root.entrySet()
        .flatMap { it.value.asJsonArray.map { it.asJsonObject } }
        .map { it.entrySet().first().let { it.key to it.value.asString } }
        .map {
            CountryCode(
                code = it.second.toInt(),
                country = it.first
            )
        }
}


private data class CountryCode(
    val code: Int,
    val country: String
)