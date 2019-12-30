package com.ifanr.tangzhi.ui.sendreview.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.inflateInto
import com.ifanr.tangzhi.ui.widgets.RatingBar

/**
 * 带有文字描述的打分控件
 */
class TextRatingBar: ConstraintLayout {

    private val text: TextView
    private val ratingBar: RatingBar
    private val ratingText by lazy {
        resources.getStringArray(R.array.ratingbar_text) }

    var onProgressChanged: (Int) -> Unit = {}

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        inflateInto(R.layout.text_rating_bar)
        text = findViewById(R.id.ratingTv)
        ratingBar = findViewById(R.id.ratingBar)
        ratingBar.onProgressChanged = { setProgress(it) }
        setProgress(0)
    }

    private fun setProgress(progress: Int) {
        text.text = ratingText.getOrNull(progress.div(20))
        onProgressChanged.invoke(progress)
    }
}