package com.ifanr.tangzhi.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.inflateInto

/**
 * 回复图标和回复数量
 */
class ReplyView: ConstraintLayout {

    private val countTv: TextView

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        inflateInto(R.layout.review_reply_view)
        countTv = findViewById(R.id.countTv)
    }

    fun setCount(count: Int) {
        countTv.text = if (count <= 0) context.getText(R.string.reply)
        else if (count > 999) "999+"
        else count.toString()
    }
}