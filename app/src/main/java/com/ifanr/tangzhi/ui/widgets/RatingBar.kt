package com.ifanr.tangzhi.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.annotation.Dimension
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.ui.icons.IcStartGoldenView
import com.ifanr.tangzhi.ui.icons.IconView
import kotlin.math.roundToInt

/**
 * 五星好评打分控件
 */
class RatingBar: ConstraintLayout {

    companion object {

        private const val STEP = 20

        @Dimension(unit = Dimension.DP)
        private const val STAR_SIZE = 30

        @Dimension(unit = Dimension.DP)
        private const val GAP = 9
    }

    private lateinit var goldenStars: List<IcStartGoldenView>
    var editMode = false

    constructor(context: Context?) : super(context) { init(null) }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init(attrs) }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init(attrs) }

    private fun init(attrs: AttributeSet?) {
        var starSize = 0
        var starGap = 0
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.RatingBar)
            starSize = ta.getDimensionPixelSize(R.styleable.RatingBar_ratingBarStarSize,
                context.dp2px(STAR_SIZE))
            starGap = ta.getDimensionPixelSize(R.styleable.RatingBar_ratingBarStarGap,
                context.dp2px(GAP))
            ta.recycle()
        }

        LayoutInflater.from(context).inflate(R.layout.rating_bar, this, true)
        children.map { it as IconView }.forEachIndexed { i, view ->
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, starSize.toFloat())
            if (i in 1..4) {
                (view.layoutParams as LayoutParams).marginStart = starGap
            }
        }
        goldenStars = (5..9).map { getChildAt(it) as IcStartGoldenView }
        goldenStars.forEachIndexed { i, v ->
            v.setOnClickListener {
                if (editMode)
                    setProgress(i.times(STEP).plus(STEP))
            }
        }
        setProgress(0)
    }

    fun setProgress(@IntRange(from = 0, to = 100) progress: Int) {
        val safeProgress = progress.coerceIn(0, 100)
        val star = safeProgress.div(STEP)
        val rem = safeProgress.rem(STEP)

        goldenStars.forEach { it.starSize = IcStartGoldenView.Size.NONE }
        goldenStars.take(star).forEach { it.starSize = IcStartGoldenView.Size.FULL }
        if (rem > 0) {
            goldenStars[star].starSize =
                if (rem <= STEP.div(2)) IcStartGoldenView.Size.LEFT else IcStartGoldenView.Size.FULL
        }
    }

    fun setProgress(@FloatRange(from = 0.0, to = 10.0) progress: Float) {
        setProgress(progress.times(10).roundToInt())
    }
}