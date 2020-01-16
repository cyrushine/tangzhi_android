package com.ifanr.tangzhi.ui.widgets

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.TextAppearanceSpan
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.children
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.ext.getColorCompat
import com.ifanr.tangzhi.ext.setPadding
import com.ifanr.tangzhi.model.Comment
import kotlin.math.max

/**
 * 产品的 tag 列表
 * 流式布局
 * 从左到右，从上到下
 * 一行放不下的话，放到下一行
 * 如果一行不能完全展示，则截断到此行，剩余空间均匀分配至各行
 */
class ProductTagList: ViewGroup {

    private enum class State {
        IDLE, ANIIMATE
    }

    companion object {
        private const val TAG = "ProductTagList"
    }

    // child 之间水平的间距
    private val childHorizontalPadding = context.dp2px(8)
    // 行间距
    private val rowPadding = context.dp2px(8)
    private var state = State.IDLE

    var onTagClick: (position: Int) -> Unit = {}

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        clipChildren = false
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // 不固定高度的情况，需要算出高度
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            measureChildren(
                MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
            setMeasuredDimension(measuredWidth, calHeightNeeded())
            Log.d(TAG, "$measuredHeight")

        } else {

            // 固定高度的情况
            measureChildren(
                MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.AT_MOST)
            )
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = 0
        var top = 0
        var rowHeight = 0
        var index = 0
        var child: View
        while (index < childCount) {
            child = getChildAt(index)

            if (left + child.measuredWidth <= measuredWidth) {
                child.layout(left, top, left + child.measuredWidth, top + child.measuredHeight)
                left += child.measuredWidth + childHorizontalPadding
                rowHeight = max(rowHeight, child.measuredHeight)

                // 超过 parent window 的隐藏
                if (top + rowHeight > measuredHeight) {
                    child.visibility = View.GONE
                } else {
                    child.visibility = View.VISIBLE
                }
                index++
            } else {

                // 换行
                left = 0
                top += rowHeight + rowPadding
                rowHeight = 0
            }
        }
    }

    private fun calHeightNeeded(): Int {
        var left = 0
        var top = 0
        var rowHeight = 0
        var index = 0
        var child: View
        while (index < childCount) {
            child = getChildAt(index)

            if (left + child.measuredWidth <= measuredWidth) {
                left += child.measuredWidth + childHorizontalPadding
                rowHeight = max(rowHeight, child.measuredHeight)
                index++
            } else {

                // 换行
                left = 0
                top += rowHeight + rowPadding
                rowHeight = 0
            }
        }
        return top + rowHeight
    }

    fun setData(tags: List<ProductTag>) {
        tags.forEachIndexed { index, tag ->
            val child = if (index < childCount)
                getChildAt(index) as ProductTagTextView
            else
                ProductTagTextView(context).apply { addView(this) }
            child.setData(tag)
            child.setOnClickListener { onChildClick(index) }
        }
        if (childCount > tags.size) {
            removeViews(tags.size, childCount - tags.size)
        }
        requestLayout()
    }


    private fun onChildClick(position: Int) {
        if (state == State.IDLE) {
            state = State.ANIIMATE
            val child = getChildAt(position)
            child.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .setDuration(150)
                .withEndAction {
                    child.animate()
                        .scaleY(1f)
                        .scaleX(1f)
                        .setDuration(150)
                        .withEndAction {
                            state = State.IDLE
                            onTagClick.invoke(position)
                        }
                        .start()
                }
                .start()
        }
    }

}

/**
 * 单个产品标签
 */
class ProductTagTextView: AppCompatTextView {

    private val drawable: ShapeDrawable

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setPadding(intArrayOf(6, 4, 6, 4))
        setTextColor(context.getColorCompat(R.color.base_3a))
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11f)
        drawable =
            ShapeDrawable(
                RoundRectShape(
                FloatArray(8) { context.dp2px(3f) }, null, null
            )
            )
        background = drawable
        ellipsize = TextUtils.TruncateAt.MIDDLE
    }

    fun setData(item: ProductTag) {
        text = SpannableStringBuilder()
            .append(item.content).append(" ")
            .append(
                "x${item.count.coerceIn(0, 99)}",
                TextAppearanceSpan(
                    context, if (item.voted)
                        R.style.AppTextAppearance_ProductTagCount_Voted
                    else
                        R.style.AppTextAppearance_ProductTagCount
                ),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        drawable.paint.color = item.bgColor
        drawable.invalidateSelf()
    }
}

data class ProductTag (
    val content: String,
    val count: Int,
    @ColorInt val bgColor: Int,
    // 点赞后会标红
    val voted: Boolean = false
) {
    constructor(source: Comment): this(
        content = source.content,
        count = source.upvote,
        bgColor = source.theme,
        voted = source.voted
    )
}

private data class LayoutInfo (
    val row: Int,
    val left: Int,
    var top: Int = 0
)