package com.ifanr.tangzhi.ui.product.comments.review.widget

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.util.TypedValue
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.inputmethod.EditorInfoCompat
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getColorCompat
import com.ifanr.tangzhi.ext.inflateInto
import com.ifanr.tangzhi.ext.setPadding
import com.ifanr.tangzhi.ui.icons.IconSpan

class TagInputView: ConstraintLayout {

    private val input: EditText

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setBackgroundResource(R.drawable.tag_input_view)
        setPadding(intArrayOf(11, 12, 11, 12))
        inflateInto(R.layout.tag_input_view)
        input = findViewById(R.id.tag_dialog_input_et)
        input.hint = SpannableStringBuilder()
            .append(context.getText(R.string.ic_plus_icon_popup), IconSpan(context), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            .append(context.getText(R.string.product_tag_dialog_add))
    }
}