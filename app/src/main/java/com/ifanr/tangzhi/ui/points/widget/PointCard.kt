package com.ifanr.tangzhi.ui.points.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.inflateInto
import java.text.NumberFormat

/**
 * 积分卡片
 */
class PointCard: ConstraintLayout {

    private val count: TextView
    private val format by lazy { NumberFormat.getNumberInstance().apply {
        isGroupingUsed = true
    }}

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        inflateInto(R.layout.point_card)
        setBackgroundResource(R.drawable.points_card_bg)
        count = findViewById(R.id.count)
    }

    fun setData(data: Int) {
        count.text = format.format(data)
    }
}