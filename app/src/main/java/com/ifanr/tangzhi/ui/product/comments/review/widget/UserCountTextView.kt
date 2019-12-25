package com.ifanr.tangzhi.ui.product.comments.review.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.ifanr.tangzhi.R
import java.text.NumberFormat

class UserCountTextView: AppCompatTextView {

    private val nf by lazy {
        NumberFormat.getNumberInstance().apply {
            maximumFractionDigits = 1
        }
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setCount(count: Int) {
        val countStr = if (count > 1000) {
            nf.format(count / 1000f) + "k"
        } else {
            count.toString()
        }
        text = context.getString(R.string.product_review_indexes_user_count, countStr)
    }
}