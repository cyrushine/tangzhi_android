package com.ifanr.tangzhi.ui.signin.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getColorCompat
import com.ifanr.tangzhi.ext.setPadding

/**
 * 登录模块通用的按钮样式
 */
class FormButton: AppCompatTextView {

    constructor(context: Context?) : super(context) { init(null) }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init(attrs) }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init(attrs) }

    init {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        gravity = Gravity.CENTER
        setPadding(intArrayOf(0, 14, 0, 14))
    }

    private fun init(attrs: AttributeSet?) {
        var bg = R.drawable.form_button_bg
        var textColor = context.getColorCompat(R.color.white)
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.FormButton)
            when (ta.getInt(R.styleable.FormButton_formButtonStyle, 0)) {
                1 -> {
                    bg = R.drawable.form_button_bg_stroke
                    textColor = context.getColorCompat(R.color.base_88)
                }
                else -> {
                    bg = R.drawable.form_button_bg
                    textColor = context.getColorCompat(R.color.white)
                    typeface = Typeface.DEFAULT_BOLD
                }
            }
        }
        setBackgroundResource(bg)
        setTextColor(textColor)
    }

}