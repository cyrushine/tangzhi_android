package com.ifanr.tangzhi.ui.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.dp2px

class EditorChoiceImageView: AppCompatImageView {

    companion object {
        private const val WIDTH = 74
        private const val HEIGHT = 26
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setImageResource(R.drawable.editor_choice)
        scaleType = ScaleType.FIT_XY
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthSpec = widthMeasureSpec
        var heightSpec = heightMeasureSpec

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED)
            widthSpec = MeasureSpec.makeMeasureSpec(context.dp2px(WIDTH), MeasureSpec.EXACTLY)

        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED)
            heightSpec = MeasureSpec.makeMeasureSpec(context.dp2px(HEIGHT), MeasureSpec.EXACTLY)

        super.onMeasure(widthSpec, heightSpec)
    }
}