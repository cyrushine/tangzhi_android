package com.ifanr.tangzhi.ui.icons

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.annotation.StringRes
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getColorCompat
import kotlin.properties.Delegates

/**
 * 灰色的星星🌟
 */
class IcStarFullGreyView: IconView {

    companion object {
        private val GREY = Color.rgb(216, 216, 216)
    }

    constructor(context: Context?) : super(context) { init() }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init() }

    private fun init() {
        setText(R.string.ic_star_full)
        setTextColor(GREY)
    }
}

/**
 * 金色的小星星🌟
 * 可以是 full, left, right
 */
class IcStartGoldenView: IconView {

    enum class Size(@StringRes val text: Int) {
        FULL(text = R.string.ic_star_full),
        LEFT(text = R.string.ic_star_left),
        RIGHT(text = R.string.ic_star_right),
        NONE(text = R.string.ic_star_full)
    }

    constructor(context: Context?) : super(context) { init() }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init() }

    var starSize: Size by Delegates.observable(Size.FULL) { _, _, value ->
        setText(value.text)
        setTextColor(context.getColorCompat(when (value) {
            Size.NONE -> R.color.transparent
            else -> R.color.golden_start
        }))
    }

    private fun init() {
        starSize = Size.FULL
    }

}