package com.ifanr.android.tangzhi.ui.product.indexes

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.FloatRange
import androidx.constraintlayout.widget.ConstraintLayout
import com.ifanr.android.tangzhi.R
import com.ifanr.android.tangzhi.ext.setPadding
import com.ifanr.android.tangzhi.ui.widgets.RatingBar
import com.ifanr.android.tangzhi.ui.widgets.ScoreTextView

class IndexesBox: ConstraintLayout {

    private val scoreTv: ScoreTextView
    private val ratingBar: RatingBar

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        LayoutInflater.from(context).inflate(R.layout.product_indexes_box, this, true)
        scoreTv = findViewById(R.id.scoreTv)
        ratingBar = findViewById(R.id.ratingbar)
        setBackgroundResource(R.drawable.product_indexes_box_bg)
        setPadding(intArrayOf(18, 17, 18, 17))
    }

    fun setScore(@FloatRange(from = 0.0, to = 10.0) score: Float) {
        scoreTv.setScore(score)
        ratingBar.setProgress(score)
    }
}