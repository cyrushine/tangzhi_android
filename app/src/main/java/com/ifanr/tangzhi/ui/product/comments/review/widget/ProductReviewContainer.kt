package com.ifanr.tangzhi.ui.product.comments.review.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.OverScroller
import androidx.core.view.*
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.util.axesToString
import com.ifanr.tangzhi.util.typeToString
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * 第一个 child 是 header
 * 第二个 child 是 toolBar
 * 第三个 child 是 list
 */
class ProductReviewContainer: ViewGroup, NestedScrollingParent3, NestedScrollingChild3 {

    companion object {
        private const val TAG = "ProductReviewList"
    }

    private val scroller: OverScroller
    private val scrollingChildHelper = NestedScrollingChildHelper(this)
    private var pointerId = 0
    private var lastY = 0f
    private val touchSlop by lazy { ViewConfiguration.get(context).scaledTouchSlop }

    private val header: View
        get() = getChildAt(0)

    private val toolBar: View
        get() = getChildAt(1)

    private val list: View
        get() = getChildAt(2)

    /**
     * 可以往上/往下滚动的高度（> 0）
     */
    private val scrollableHeight: Int
        get() = header.measuredHeight - scrollY


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
        scroller = OverScroller(context)
        scrollingChildHelper.isNestedScrollingEnabled = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        measureChild(header,
            MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))

        measureChild(toolBar,
            MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))

        measureChild(list,
            MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(
                measuredHeight - toolBar.measuredHeight, MeasureSpec.EXACTLY))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = 0
        var top = 0
        val right = measuredWidth
        var bottom = header.measuredHeight
        header.layout(left, top, right, bottom)

        top += header.measuredHeight
        bottom += toolBar.measuredHeight
        toolBar.layout(left, top, right, bottom)

        top += toolBar.measuredHeight
        bottom += list.measuredHeight
        list.layout(left, top, right, bottom)
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
            invalidate()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        scrollingChildHelper.onDetachedFromWindow()
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return super.onTouchEvent(event)
    }



    /*************************** NestedScrollingParent3 start *********************************/


    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        startNestedScroll(axes, type)
        Log.d(TAG, "onStartNestedScroll, ${axesToString(axes)}, ${typeToString(type)}")
        return axes.and(ViewCompat.SCROLL_AXIS_VERTICAL) == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        Log.d(TAG, "onNestedScrollAccepted, ${axesToString(axes)}, ${typeToString(type)}")
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        stopNestedScroll(type)
        Log.d(TAG, "onStopNestedScroll, ${typeToString(type)}")
    }

    /**
     * dy > 0 往上滚动
     * dy < 0 往下滚动
     */
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        // rv 往上滚动时，优先滚动 header
        if (dy > 0 && scrollableHeight > 0) {
            val maxDy = min(scrollableHeight, dy)
            scrollBy(0, maxDy)
            consumed[1] = maxDy
        }
        Log.d(TAG, "onNestedPreScroll, $dy, ${consumed.joinToString()}, ${typeToString(type)}")
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        // rv 往下滚动时，优先滚动 rv，再往下滚动 header
        if (dyUnconsumed < 0) {
            val maxDy = -min(abs(dyUnconsumed), scrollY)
            scrollBy(0, maxDy)
            consumed[1] = maxDy

            if (dyUnconsumed < consumed[1] && hasNestedScrollingParent(type)) {
                val parentConsumed = IntArray(2) { 0 }
                dispatchNestedScroll(
                    dxConsumed,
                    dyConsumed + consumed[1],
                    dxUnconsumed,
                    dyUnconsumed + abs(consumed[1]),
                    null,
                    type,
                    parentConsumed)
                consumed[1] += parentConsumed[1]
            }
        }
        Log.d(
            TAG, "onNestedScroll, $dyConsumed, $dyUnconsumed, " +
                "${consumed.joinToString()}, ${typeToString(type)}")
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        this.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed,
            dyUnconsumed, type, IntArray(2) { 0 })
    }

    /*************************** NestedScrollingParent3 end *********************************/







    /*************************** NestedScrollingChild3 start *********************************/

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?,
        type: Int,
        consumed: IntArray
    ) {
        scrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed,
            dyUnconsumed, offsetInWindow, type, consumed)
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        return scrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed,
            dyUnconsumed, offsetInWindow, type)
    }

    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        return scrollingChildHelper.startNestedScroll(axes, type)
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        return scrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)
    }

    override fun stopNestedScroll(type: Int) {
        scrollingChildHelper.stopNestedScroll(type)
    }

    override fun hasNestedScrollingParent(type: Int): Boolean {
        return scrollingChildHelper.hasNestedScrollingParent(type)
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?
    ): Boolean {
        return scrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
            dxUnconsumed, dyUnconsumed, offsetInWindow)
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?
    ): Boolean {
        return scrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return scrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY)
    }

    override fun dispatchNestedFling(
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        return scrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed)
    }

    /*************************** NestedScrollingChild3 start *********************************/

}