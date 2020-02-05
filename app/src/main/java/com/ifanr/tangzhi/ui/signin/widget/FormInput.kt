package com.ifanr.tangzhi.ui.signin.widget

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.inflateInto

/**
 * 登录模块通用的 input 样式
 */
class FormInput: ConstraintLayout {

    private val input: EditText
    var onTextChangedListener: (String) -> Unit = {}

    constructor(context: Context?) : super(context) { init(null) }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init(attrs) }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init(attrs) }

    init {
        inflateInto(R.layout.form_input)
        input = findViewById(R.id.formInputEt)
        input.addTextChangedListener { onTextChangedListener.invoke(it?.toString() ?: "") }
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.FormInput)
            input.inputType = when (ta.getInt(R.styleable.FormInput_formInputType, 0)) {
                1 -> EditorInfo.TYPE_CLASS_PHONE
                2 -> EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
                3 -> EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                else -> EditorInfo.TYPE_CLASS_TEXT
            }
            input.hint = ta.getText(R.styleable.FormInput_formInputHint)
            ta.recycle()
        }
    }
}