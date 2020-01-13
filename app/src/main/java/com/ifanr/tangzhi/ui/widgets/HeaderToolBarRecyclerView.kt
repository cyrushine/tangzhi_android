package com.ifanr.tangzhi.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.Scroller
import androidx.core.view.NestedScrollingParent3
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * 具有 header，toolbar，RecyclerView 三段式的列表
 */
class HeaderToolBarRecyclerView: ViewGroup, NestedScrollingParent3 {

    companion object {
        private const val TAG = "HeaderToolBarRecycler"
    }

    private lateinit var header: View
    private lateinit var toolBar: View
    private lateinit var list: RecyclerView

    // 向上可以滚动的距离（> 0）
    private val scrollUpAvailable: Int
        get() = header.measuredHeight - scrollY

    // 向下可以滚动的距离（> 0）
    private val scrollDownAvailable: Int
        get() = scrollY

    private val vc by lazy { ViewConfiguration.get(context) }

    private var scrolling = false
    private var touchPointerId = 0
    private var touchY = 0f
    private val scroller = Scroller(context)
    private val velocityTracker = VelocityTracker.obtain()
    private var listScrollY = 0

    // 处理自身的滚动事件
    private val onTouchListener = OnTouchListener { v, event ->
        val action = event.actionMasked
        val pointerId = event.getPointerId(event.actionIndex)
        val y = event.getY(event.actionIndex)
        val x = event.getX(event.actionIndex)

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                touchPointerId = pointerId
                touchY = y
                velocityTracker.addMovement(event)
            }

            MotionEvent.ACTION_MOVE ->  {
                if (touchPointerId == pointerId) {
                    velocityTracker.addMovement(event)
                    var scroll = (touchY - y).roundToInt()

                    if (!scrolling) {
                        scrolling = (abs(scroll) > vc.scaledTouchSlop) &&

                                // header 收起来的时候，不能通过拉 toolBar 把 header 拉下来
                                !(scroll < 0 && scrollUpAvailable == 0)
                    } else {

                        // 开启滚动
                        touchY = y
                        scroll = if (scroll > 0) {
                            min(scrollUpAvailable, scroll)
                        } else {
                            max(-scrollDownAvailable, scroll)
                        }
                        scrollBy(0, scroll)
                    }
                }
            }

            MotionEvent.ACTION_UP -> {
                if (touchPointerId == pointerId) {
                    velocityTracker.addMovement(event)
                    velocityTracker.computeCurrentVelocity(1000)
                    var velocityY = velocityTracker.yVelocity   // pixels per second

                    // start fling
                    if (abs(velocityY) > vc.scaledMinimumFlingVelocity && scrolling) {
                        velocityY = if (velocityY < 0)
                            max(velocityY, -vc.scaledMaximumFlingVelocity.toFloat())
                        else
                            min(velocityY, vc.scaledMaximumFlingVelocity.toFloat())
                        scroller.fling(
                            0, scrollY,
                            0, -velocityY.roundToInt(),
                            0, 0,
                            Int.MIN_VALUE, Int.MAX_VALUE)
                        invalidate()
                    }

                    // cleanup
                    touchPointerId = 0
                    touchY = 0f
                    scrolling = false
                    velocityTracker.clear()
                }
            }
        }

        true
    }

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
        setOnTouchListener(onTouchListener)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        header = getChildAt(0)
        toolBar = getChildAt(1)
        list = getChildAt(2) as RecyclerView
        list.addOnScrollListener(object: RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                listScrollY += dy
            }
        })
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        measureChild(
            header,
            MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))

        measureChild(
            toolBar,
            MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        measureChild(
            list,
            MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(
                measuredHeight - toolBar.measuredHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var top = 0
        header.layout(0, top, measuredWidth, top + header.measuredHeight)
        top += header.measuredHeight

        toolBar.layout(0, top, measuredWidth, top + toolBar.measuredHeight)
        top += toolBar.measuredHeight

        list.layout(0, top, measuredWidth, top + list.measuredHeight)
    }


    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            val y = scroller.currY - scrollY

            // 向下滚动
            if (y < 0) {
                val scrollY = min(abs(y), scrollY)
                scrollBy(0, -scrollY)
                scroller.forceFinished(scrollY == 0)

                // 向上滚动
            } else {
                val availableY = header.measuredHeight - scrollY
                var dyConsumed = 0
                if (availableY > 0) {
                    dyConsumed = min(y, availableY)
                    scrollBy(0, dyConsumed)
                }

                // 如果有没有消耗掉的滚动，分发给 list
                if (y > dyConsumed) {
                    val scrollY = y - dyConsumed - listScrollY
                    list.scrollBy(0, scrollY)
                }
            }
            invalidate()
        }
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.actionMasked

        // 触摸中断 fling
        if (!scroller.isFinished && action == MotionEvent.ACTION_DOWN) {
            scroller.forceFinished(true)
        }

        return super.onInterceptTouchEvent(ev)
    }

    /*********************************** NestedScrollingParent3 *****************************************/



    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        // 向上滚动
        if (dy > 0) {
            val scroll = min(scrollUpAvailable, dy)
            scrollBy(0, scroll)
            consumed[0] = 0
            consumed[1] = scroll
        }
    }

    override fun onStopNestedScroll(target: View, type: Int) {

    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return true
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {

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
        // 向下滚动
        if (dyUnconsumed < 0 && scrollDownAvailable > 0) {
            val scroll = max(dyUnconsumed, -scrollDownAvailable)
            scrollBy(0, scroll)
            consumed[1] += scroll
        }
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {

    }



}