package com.ifanr.android.tangzhi.ui.product.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.ifanr.android.tangzhi.R
import com.ifanr.android.tangzhi.ext.dp2px

/**
 * 每个章节的 header
 */
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class SectionHeaderView: ConstraintLayout {

    private val titleTv: TextView
    private val countTv: TextView

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        LayoutInflater.from(context).inflate(R.layout.section_header_view, this, true)
        setPadding(0, context.dp2px(40), 0, context.dp2px(10))
        titleTv = findViewById(R.id.titleTv)
        countTv = findViewById(R.id.totalTv)
    }

    @TextProp
    fun setTitle(title: CharSequence) {
        titleTv.text = title
    }

    @TextProp
    fun setCount(count: CharSequence) {
        countTv.text = count
    }

}