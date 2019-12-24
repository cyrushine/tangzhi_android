package com.ifanr.tangzhi.ui.product.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.customview.widget.ViewDragHelper
import com.ifanr.tangzhi.ext.canScrollDown
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.util.axesToString
import com.ifanr.tangzhi.util.typeToString

class ProductContainer: ViewGroup, NestedScrollingParent3 {

    enum class Status {
        REVIEW_FOLD,
        REVIEW_EXPAND,
        REVIEW_DRAGGING,
        REVIEW_SETTLING
    }

    companion object {
        private const val TAG = "ProductContainer"
    }

    private lateinit var infoPanel: NestedScrollView
    private lateinit var reviewsPanel: View
    private var status = Status.REVIEW_FOLD
    private var actionDownPointerId = 0
    private var actionLastY = 0f

    // 点评面板露出高度
    private val reviewExposeHeight = context.dp2px(50)

    // 点评面板距离 container top 的高度
    private val reviewPaddingTop = context.dp2px(50)

    private val dragCallback: ViewDragHelper.Callback = object: ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child == reviewsPanel
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return top.coerceIn(reviewPaddingTop, measuredHeight - reviewExposeHeight)
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            // 往上 fling
            if (yvel < 0 || (yvel == 0f && releasedChild.top <= (measuredHeight - reviewExposeHeight) * 0.7)) {
                if (dragHelper.settleCapturedViewAt(0, reviewPaddingTop)) {
                    invalidate()
                }
            } else {

                // 往下 fling
                if (dragHelper.settleCapturedViewAt(0, measuredHeight - reviewExposeHeight)) {
                    invalidate()
                }
            }
        }

        override fun onViewDragStateChanged(state: Int) {
            status = when (state) {
                ViewDragHelper.STATE_SETTLING -> Status.REVIEW_SETTLING
                ViewDragHelper.STATE_DRAGGING -> Status.REVIEW_DRAGGING
                else -> if (reviewsPanel.top == reviewPaddingTop)
                    Status.REVIEW_EXPAND
                else
                    Status.REVIEW_FOLD
            }
        }
    }
    private val dragHelper = ViewDragHelper.create(this, dragCallback)

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

    override fun onFinishInflate() {
        super.onFinishInflate()
        infoPanel = getChildAt(0) as NestedScrollView
        reviewsPanel = getChildAt(1)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        measureChild(
            infoPanel,
            MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(measuredHeight - reviewExposeHeight, MeasureSpec.EXACTLY))

        measureChild(
            reviewsPanel,
            MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(measuredHeight - reviewPaddingTop, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = 0
        var top = 0
        var right = infoPanel.measuredWidth
        var bottom = infoPanel.measuredHeight
        infoPanel.layout(left, top, right, bottom)

        top = measuredHeight - reviewExposeHeight
        right = reviewsPanel.measuredWidth
        bottom = top + reviewsPanel.measuredHeight
        reviewsPanel.layout(left, top, right, bottom)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.actionMasked
        val actionIndex = ev.actionIndex
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                actionDownPointerId = ev.getPointerId(actionIndex)
                actionLastY = ev.y
                dragHelper.shouldInterceptTouchEvent(ev)
            }
        }

        // review 收起的时候，通过露出部分拉起来
        val condition1 = status == Status.REVIEW_FOLD
                && dragHelper.shouldInterceptTouchEvent(ev)

        // review 展开的时候，通过 drag 露出部分的高度收起来
        val hotSpot = (reviewsPanel.top.toFloat() ..
                (reviewsPanel.top + reviewExposeHeight).toFloat())
        val condition2 = status == Status.REVIEW_EXPAND &&
                reviewsPanel.scrollY == 0 &&
                hotSpot.contains(ev.y)

        // review 展开的时候，滚动到顶部继续往下拉，可以把 review 收起来
        var condition3 = false
        if (status == Status.REVIEW_EXPAND && reviewsPanel.scrollY == 0 &&
            action == MotionEvent.ACTION_MOVE && ev.getPointerId(actionIndex) == actionDownPointerId) {
            val distance = ev.y - actionLastY
            if (distance >= dragHelper.touchSlop) {
                dragHelper.shouldInterceptTouchEvent(ev)
                dragHelper.captureChildView(reviewsPanel, actionDownPointerId)
                condition3 = true
            }
        }

        var condition4 = false
        if (status == Status.REVIEW_FOLD && !infoPanel.canScrollDown() &&
            action == MotionEvent.ACTION_MOVE && ev.getPointerId(actionIndex) == actionDownPointerId) {
            val distance = actionLastY - ev.y
            if (distance >= dragHelper.touchSlop) {
                dragHelper.shouldInterceptTouchEvent(ev)
                dragHelper.captureChildView(reviewsPanel, actionDownPointerId)
                condition4 = true
            }
        }

        return condition1 || condition2 || condition3 || condition4 || super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        dragHelper.processTouchEvent(event)
        return true
    }

    override fun computeScroll() {
        super.computeScroll()
        if (dragHelper.continueSettling(true)) {
            invalidate()
        }
    }

    /************************** NestedScrollingParent3 start ******************************/

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {

        // info 往下滚动，review 先消费，info 后消费
        /*if (target == infoPanel) {
            val maxDy = -min(abs(dy), scrollY)
            if (maxDy < 0) {
                scrollBy(0, maxDy)
                consumed[1] = maxDy
            }
        }*/

        Log.d(TAG, "onNestedPreScroll, $dy, ${consumed.joinToString()}, ${typeToString(type)}")
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        Log.d(TAG, "onStopNestedScroll, ${typeToString(type)}")
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        Log.d(TAG, "onStartNestedScroll, ${axesToString(axes)}, ${typeToString(type)}")
        /*return axes.and(ViewCompat.SCROLL_AXIS_VERTICAL) == ViewCompat.SCROLL_AXIS_VERTICAL*/
        return false
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        Log.d(TAG, "onNestedScrollAccepted, ${axesToString(axes)}, ${typeToString(type)}")
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

        // info 往上滚动，info 先消费，review 后消费
        /*if (target == infoPanel && dyUnconsumed > 0) {
            val maxDy = min(dyUnconsumed, reviewsPanel.measuredHeight - scrollY - reviewExposeHeight)
            if (maxDy > 0) {
                scrollBy(0, maxDy)
                consumed[1] = maxDy
            }
        }*/

        // review 往下滚动，review 先消费，container 后消费
        /*if (target == reviewsPanel && dyUnconsumed < 0 && scrollY > 0) {
            val maxDy = -min(abs(dyUnconsumed), scrollY)
            if (maxDy < 0) {
                scrollBy(0, maxDy)
                consumed[1] = maxDy
            }
        }*/

        Log.d(TAG, "onNestedScroll, $dyConsumed, $dyUnconsumed, " +
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

    /************************** NestedScrollingParent3 end ******************************/





}