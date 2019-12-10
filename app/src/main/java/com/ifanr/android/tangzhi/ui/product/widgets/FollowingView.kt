package com.ifanr.android.tangzhi.ui.product.widgets

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.Dimension
import androidx.annotation.MainThread
import androidx.constraintlayout.widget.ConstraintLayout
import com.ifanr.android.tangzhi.R
import com.ifanr.android.tangzhi.ext.dp2px
import com.ifanr.android.tangzhi.ext.getColorCompat
import com.ifanr.android.tangzhi.ui.widgets.CompleteRoundedRectDrawable
import kotlin.properties.Delegates

/**
 * 关注按钮
 */
class FollowingView: ConstraintLayout {

    enum class State {
        UN_FOLLOW, FOLLOWED
    }

    companion object {
        @Dimension(unit = Dimension.DP)
        private const val WIDTH = 78

        @Dimension(unit = Dimension.DP)
        private const val HEIGHT = 32
    }

    private lateinit var icon: View
    private lateinit var followText: TextView
    private lateinit var followedText: TextView

    var state: State by Delegates.observable(
        State.UN_FOLLOW
    ) { _, _, new ->
        when (new) {
            State.UN_FOLLOW -> {
                followedText.visibility = View.GONE
                icon.visibility = View.VISIBLE
                followText.visibility = View.VISIBLE
            }

            State.FOLLOWED -> {
                followedText.visibility = View.VISIBLE
                icon.visibility = View.GONE
                followText.visibility = View.GONE
            }
        }
        requestLayout()
    }
    @MainThread get
    @MainThread set


    constructor(context: Context?) : super(context) { init() }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init() }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.following_view, this, true)
        icon = findViewById(R.id.icon)
        followText = findViewById(R.id.followText)
        followedText = findViewById(R.id.followedText)
        state = State.UN_FOLLOW
        background = CompleteRoundedRectDrawable().apply {
            paint.color = context.getColorCompat(R.color.following_view_border_color)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = context.dp2px(1).toFloat()
        }
    }

    @MainThread
    fun toggleState() {
        state = when (state) {
            State.UN_FOLLOW -> State.FOLLOWED
            State.FOLLOWED -> State.UN_FOLLOW
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(context.dp2px(WIDTH), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(context.dp2px(HEIGHT), MeasureSpec.EXACTLY))
    }
}