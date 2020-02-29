package com.ifanr.tangzhi.ui.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import kotlin.math.ceil

/**
 * 隐藏手机号中间的四个字符
 */
class PhoneTextView: AppCompatTextView {

    companion object {
        private const val HIDDEN_LENGHT = 4
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setPhone(phone: String) {
        val len = phone.length
        if (len <= HIDDEN_LENGHT) {
            text = "****"
        } else {
            val start = ceil(((len - HIDDEN_LENGHT) / 2.0f)).toInt()
            text = phone.toCharArray().let {
                it[start] = '*'
                it[start + 1] = '*'
                it[start + 2] = '*'
                it[start + 3] = '*'
                String(it)
            }
        }
    }
}