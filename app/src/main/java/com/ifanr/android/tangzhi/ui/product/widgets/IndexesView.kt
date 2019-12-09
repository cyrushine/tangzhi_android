package com.ifanr.android.tangzhi.ui.product.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.annotation.FloatRange
import androidx.constraintlayout.widget.ConstraintLayout
import com.ifanr.android.tangzhi.R
import com.ifanr.android.tangzhi.ui.widgets.RatingBar
import com.ifanr.android.tangzhi.ui.widgets.ScoreTextView
import java.math.RoundingMode
import java.text.NumberFormat
import kotlin.math.roundToInt

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

