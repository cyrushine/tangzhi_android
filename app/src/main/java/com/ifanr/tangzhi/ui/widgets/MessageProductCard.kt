package com.ifanr.tangzhi.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getColorCompat
import com.ifanr.tangzhi.ext.inflateInto
import com.ifanr.tangzhi.ext.setPadding

/**
 * 「我的消息」里的「产品卡片」
 */
class MessageProductCard: ConstraintLayout {

    private val contentTv: TextView
    private val nameTv: TextView

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        inflateInto(R.layout.message_product_card)
        contentTv = findViewById(R.id.messageProductCardContentTv)
        nameTv = findViewById(R.id.messageProductCardNameTv)
        setPadding(intArrayOf(10, 10, 10, 10))
        setBackgroundResource(R.drawable.message_product_card)
        nameTv.background = RoundedRectDrawable(color = context.getColorCompat(R.color.base_f2))
    }

    fun setContent(text: String) {
        contentTv.text = text
    }

    @SuppressLint("SetTextI18n")
    fun setName(text: String) {
        nameTv.text = "${context.getString(R.string.ic_sugar)} " +
                "${context.getString(R.string.product)}：$text " +
                context.getString(R.string.ic_small_arrow)
    }
}