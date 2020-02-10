package com.ifanr.tangzhi.ui.signin.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getColorCompat
import com.ifanr.tangzhi.ext.inflateInto
import com.ifanr.tangzhi.util.countDown
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlin.properties.Delegates

/**
 * 登录模块通用的验证码 input 样式
 */
class FormSmsCodeInput: ConstraintLayout {

    interface Listener {
        fun onTextChanged(text: String) {}

        /**
         * only invoke when [delayValue] == 0
         */
        fun onSendSmsCodeClick() {}
    }

    private val input: EditText
    private lateinit var operation: TextView
    private var countDownTask: Disposable? = null

    var listener: Listener = object : Listener {}

    /**
     * <= 0，显示「获取验证码」
     * > 0，显示「x 秒后重新获取验证码」
     */
    private var delayValue: Int by Delegates.observable(0) { _, _, newValue ->
        if (newValue <= 0) {
            operation.setText(R.string.sign_in_by_phone_send_code)
            operation.setTextColor(context.getColorCompat(R.color.base_red))
        } else {
            operation.text = context.getString(R.string.sign_in_by_phone_send_delay, newValue.toString())
            operation.setTextColor(context.getColorCompat(R.color.base_88))
        }
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init(attrs) }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init(attrs) }

    init {
        inflateInto(R.layout.form_sms_code_input)
        input = findViewById(R.id.formInputEt)
        operation = findViewById(R.id.operation)
        input.addTextChangedListener {
            listener.onTextChanged(it?.toString() ?: "")
        }
        operation.setOnClickListener {
            if (delayValue <= 0) {
                listener.onSendSmsCodeClick()
                input.requestFocus()
            }
        }
        delayValue = 0
    }

    fun setOnEditorActionListener(l: TextView.OnEditorActionListener) {
        input.setOnEditorActionListener(l)
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.FormSmsCodeInput)
            input.imeOptions = when (ta.getInt(R.styleable.FormSmsCodeInput_formSmsCodeInputImeOptions, 0)) {
                4 -> EditorInfo.IME_ACTION_GO
                5 -> EditorInfo.IME_ACTION_SEARCH
                6 -> EditorInfo.IME_ACTION_SEND
                7 -> EditorInfo.IME_ACTION_NEXT
                8 -> EditorInfo.IME_ACTION_DONE
                9 -> EditorInfo.IME_ACTION_PREVIOUS
                else -> EditorInfo.IME_ACTION_UNSPECIFIED
            }
            ta.recycle()
        }
    }

    /**
     * 开始倒数
     */
    fun startCountDown() {
        if (delayValue <= 0) {
            countDownTask?.dispose()
            countDownTask = countDown(from = 60)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { delayValue = it.toInt() }
        }
    }

    override fun requestFocus(direction: Int, previouslyFocusedRect: Rect?): Boolean {
        return input.requestFocus(direction, previouslyFocusedRect)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        countDownTask?.dispose()
        delayValue = 0
    }
}