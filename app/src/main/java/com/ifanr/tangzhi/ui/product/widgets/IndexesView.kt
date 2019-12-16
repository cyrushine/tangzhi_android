package com.ifanr.tangzhi.ui.product.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.FloatRange
import androidx.constraintlayout.widget.ConstraintLayout
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ui.widgets.RatingBar
import com.ifanr.tangzhi.ui.widgets.ScoreTextView

/**
 * 模范指数
 */
class IndexesView: ConstraintLayout {

    private lateinit var scoreTv: ScoreTextView
    private lateinit var ratingBar: RatingBar

    constructor(context: Context?) : super(context) { init() }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init() }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.indexes_view, this, true)
        scoreTv = findViewById(R.id.scoreTv)
        ratingBar = findViewById(R.id.ratingbar)
    }

    fun setScore(@FloatRange(from = 0.0, to = 10.0) score: Float) {
        scoreTv.setScore(score)
        ratingBar.setProgress(score)
    }
}

