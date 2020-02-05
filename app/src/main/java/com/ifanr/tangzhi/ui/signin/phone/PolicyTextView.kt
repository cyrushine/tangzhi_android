package com.ifanr.tangzhi.ui.signin.phone

import android.content.Context
import android.net.Uri
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import com.ifanr.tangzhi.Const
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getColorCompat
import com.ifanr.tangzhi.ui.widgets.TextLinkSpan

class PolicyTextView: AppCompatTextView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setTextColor(context.getColorCompat(R.color.base_88))
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        gravity = Gravity.CENTER
        text = SpannableStringBuilder().apply {
            append(context.getText(R.string.sign_in_by_phone_policy))
            setSpan(TextLinkSpan(uri = Uri.parse(Const.userAgreementUri), ctx = context),
                20, 28, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(TextLinkSpan(uri = Uri.parse(Const.privacyPolicyUri), ctx = context),
                29, 35, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        movementMethod = LinkMovementMethod.getInstance()
    }
}