package com.ifanr.tangzhi.ui.widgets

import android.content.Context
import android.text.InputFilter
import android.text.Spanned
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatEditText
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.bytes
import kotlin.math.max

/**
 * 限制文本长度为 XX 个字符（char，一个字节）
 */
class CharLimitedEditText: AppCompatEditText {

    companion object {
        private const val TAG = "CharLimitedEditText"
    }

    private var maxChar = 9

    private val limitFilter = InputFilter { source, start, end, dest, dstart, dend ->
        // source 为空表示删除
        if (source.isNotEmpty() && end - start > 0) {
            var availableChar = maxChar - dest.bytes
            var endIndex = start
            while (endIndex < end && availableChar >= source[endIndex].bytes) {
                availableChar -= source[endIndex].bytes
                endIndex++
            }
            return@InputFilter source.subSequence(start, endIndex)
        }
        null
    }

    constructor(context: Context) : super(context) { init(null) }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { init(attrs) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init(attrs) }

    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.CharLimitedEditText)
            maxChar = ta.getInt(R.styleable.CharLimitedEditText_charLimitedEditTextMaxChar, maxChar)
            ta.recycle()
        }

        if (maxChar > 0) {
            filters = arrayOf(limitFilter) + (filters ?: arrayOf())
        }
    }
}