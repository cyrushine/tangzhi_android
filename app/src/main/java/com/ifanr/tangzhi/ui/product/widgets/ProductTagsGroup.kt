package com.ifanr.tangzhi.ui.product.widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.children
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.ext.getColorCompat
import com.ifanr.tangzhi.ui.widgets.CompleteRoundedRectDrawable
import kotlin.math.max

/**
 * 产品详情 - 评测怎么看 - tags
 */
class ProductTagsGroup: ViewGroup {

    private var padding = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        padding = context.dp2px(8)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var maxHeight = 0
        children.forEach {
            it.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            maxHeight = max(maxHeight, it.measuredHeight)
        }

        setMeasuredDimension(
            getDefaultSize(suggestedMinimumWidth, widthMeasureSpec),
            getDefaultSize(maxHeight, heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val parentHeight = measuredHeight
        var left = 0
        var top = 0
        var right = 0
        var bottom = 0

        for (child in children) {
            if (left + padding + child.measuredWidth > measuredWidth)
                break

            top = (parentHeight - child.measuredHeight) / 2
            bottom = top + child.measuredHeight
            right = left + child.measuredWidth

            child.layout(left, top, right, bottom)
            left = right + padding
        }
    }

    fun setTags(tags: List<String>) {
        removeAllViews()
        tags.map { Tag(context).apply { text = it } }
            .forEach { addView(it) }
    }

    class Tag: AppCompatTextView {

        companion object {
            private val TEXT_COLOR = Color.argb(178, 255, 255, 255)
            private const val TEXT_SIZE = 10f
            private val PADDING = arrayOf(8, 4, 8, 4)
        }

        constructor(context: Context?) : super(context)
        constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
        constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
        )

        init {
            setTextColor(TEXT_COLOR)
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE)
            setPadding(context.dp2px(PADDING[0]), context.dp2px(PADDING[1]),
                context.dp2px(PADDING[2]), context.dp2px(PADDING[3]))
            background = CompleteRoundedRectDrawable().apply {
                paint.color = context.getColorCompat(R.color.product_post_tag_bg)
            }
        }
    }
}