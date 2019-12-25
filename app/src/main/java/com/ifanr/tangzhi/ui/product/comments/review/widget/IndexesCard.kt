package com.ifanr.tangzhi.ui.product.comments.review.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.inflateInto
import com.ifanr.tangzhi.ext.setPadding
import com.ifanr.tangzhi.ui.widgets.RatingBar
import com.ifanr.tangzhi.ui.widgets.ScoreTextView

class IndexesCard: ConstraintLayout {

    private val scoreTv: ScoreTextView
    private val ratingBar: RatingBar
    private val userCountTv: UserCountTextView
    private val orgCountTv: TextView

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        inflateInto(R.layout.indexes_card)
        setPadding(intArrayOf(11, 12, 11, 12))
        scoreTv = findViewById(R.id.scoreTv)
        ratingBar = findViewById(R.id.ratingbar)
        userCountTv = findViewById(R.id.userCountTv)
        orgCountTv = findViewById(R.id.orgCountTv)
        setBackgroundResource(R.drawable.product_review_indexes_card_bg)
    }

    fun set(rating: Float, reviewCount: Int, orgCount: Int) {
        ratingBar.setProgress(rating)
        scoreTv.setScore(rating)
        userCountTv.setCount(reviewCount)
        orgCountTv.text = context.getString(R.string.product_review_indexes_org_count, orgCount)
    }
}