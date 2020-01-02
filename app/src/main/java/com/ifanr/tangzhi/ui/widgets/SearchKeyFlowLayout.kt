package com.ifanr.tangzhi.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.children
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.ext.getColorCompat
import com.ifanr.tangzhi.ext.setPadding

/**
 * 搜索历史和热门推荐的流式布局
 */
class SearchKeyFlowLayout: ViewGroup {

    private val itemPadding by lazy { context.dp2px(10) }
    private val rowPadding by lazy { context.dp2px(10) }
    private val childrenLeftTop = ArrayList<Point>()

    var onItemClick: (position: Int) -> Unit = {}

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

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (childCount > 0) {
            measureChildren(
                MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )

            childrenLeftTop.clear()
            val rowHeight = getChildAt(0).measuredHeight
            var position = 0
            var left = 0
            var top = 0
            var child: View
            while (position < childCount) {
                child = getChildAt(position)
                if (left + child.measuredWidth <= measuredWidth) {
                    childrenLeftTop.add(position, Point(left, top))
                    left += child.measuredWidth + itemPadding
                    position++

                    // 换行
                } else {
                    left = 0
                    top += rowHeight + rowPadding
                }
            }

            setMeasuredDimension(measuredWidth, top + rowHeight)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount > 0) {
            children.forEachIndexed { index, child ->
                val position = childrenLeftTop.getOrNull(index)
                if (position != null) {
                    child.layout(
                        position.x,
                        position.y,
                        position.x + child.measuredWidth,
                        position.y + child.measuredHeight
                    )
                } else {
                    child.layout(0, 0, 0, 0)
                }
            }
        }
    }

    fun setData(data: List<String>) {
        data.forEachIndexed { index, s ->
            getOrCreateChild(index).text = s
        }
        val rest = childCount - data.size
        if (rest > 0) {
            repeat(rest) { removeViewAt(rest) }
        }
        children.forEachIndexed { index, child ->
            child.setOnClickListener { onItemClick.invoke(index) }
        }
        requestLayout()
    }

    private fun getOrCreateChild(index: Int): SearchKeyTextView =
        if (index < childCount)
            getChildAt(index) as SearchKeyTextView
        else
            SearchKeyTextView(context).also { addView(it) }
}

class SearchKeyTextView: AppCompatTextView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        setTextColor(context.getColorCompat(R.color.base_12))
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.END
        setPadding(intArrayOf(16, 9, 16, 9))
        background = RoundedRectDrawable(context.getColorCompat(R.color.base_f4))
    }
}