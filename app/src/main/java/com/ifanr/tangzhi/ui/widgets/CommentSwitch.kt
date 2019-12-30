package com.ifanr.tangzhi.ui.widgets

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getColorCompat
import com.ifanr.tangzhi.ext.setPadding
import kotlin.math.abs
import kotlin.properties.Delegates

/**
 * 「精选」、「最热」和「最新」的开关
 */
class CommentSwitch: ViewGroup {

    companion object {
        private const val TAG = "CommentSwitch"
    }

    enum class Type(@StringRes val text: Int) {
        EDITOR_CHOICE(text = R.string.comment_type_editor_choice),
        HOTTEST(text = R.string.comment_type_hottest),
        LATEST(text = R.string.comment_type_latest)
    }

    enum class State {
        IDLE, ANIMATE
    }

    private val editorChoice: OptionTextView
    private val hottest: OptionTextView
    private val latest: OptionTextView
    private val indicator: View
    private var value: Type by Delegates.observable(
        Type.EDITOR_CHOICE)
    { _, oldValue, newValue ->
        if (oldValue != newValue)
            onValueChanged(newValue)
    }

    private var state = State.IDLE
    private val selectedOptiion: OptionTextView
        get() = findOption(value)

    private val allOptions: Array<OptionTextView>
        get() = arrayOf(editorChoice, hottest, latest)

    var onValueChanged: (value: Type) -> Unit = {}

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
        indicator = View(context).apply {
            background = RoundCapRectDrawable(Color.WHITE)
            addView(this)
        }
        editorChoice = OptionTextView(context).apply {
            setType(Type.EDITOR_CHOICE)
            isSelected = true
            addView(this)
            setOnClickListener { setValue(Type.EDITOR_CHOICE) }
        }
        hottest = OptionTextView(context).apply {
            setType(Type.HOTTEST)
            addView(this)
            setOnClickListener { setValue(Type.HOTTEST) }
        }
        latest = OptionTextView(context).apply {
            setType(Type.LATEST)
            addView(this)
            setOnClickListener { setValue(Type.LATEST) }
        }
        setPadding(intArrayOf(2, 2, 2, 2))
        background = RoundCapRectDrawable(context.getColorCompat(R.color.base_f4))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        arrayOf(editorChoice, hottest, latest).forEach {
            it.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
        }
        indicator.measure(
            MeasureSpec.makeMeasureSpec(editorChoice.measuredWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(editorChoice.measuredHeight, MeasureSpec.EXACTLY))
        setMeasuredDimension(
            editorChoice.measuredWidth + hottest.measuredWidth + latest.measuredWidth
                    + paddingLeft + paddingRight,
            editorChoice.measuredHeight + paddingTop + paddingBottom)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = paddingLeft
        var top = paddingTop
        editorChoice.layout(
            left, top, left + editorChoice.measuredWidth, top + editorChoice.measuredHeight)
        left += editorChoice.measuredWidth

        hottest.layout(left, top, left + hottest.measuredWidth, top + hottest.measuredHeight)
        left += hottest.measuredWidth

        latest.layout(left, top, left + latest.measuredWidth, top + latest.measuredHeight)

        left = when (value) {
            Type.EDITOR_CHOICE -> editorChoice.left
            Type.HOTTEST -> hottest.left
            Type.LATEST -> latest.left
        }
        indicator.layout(
            left, top, left + indicator.measuredWidth, top + indicator.measuredHeight)
    }

    fun setValue(v: Type, animate: Boolean = true) {
        if (value != v && state == State.IDLE) {
            if (animate) {
                indicator.animate()
                    .translationX((findOption(v).left - selectedOptiion.left).toFloat())
                    .setDuration(abs(value.ordinal - v.ordinal) * 80L)
                    .withStartAction { state = State.ANIMATE }
                    .withEndAction {
                        state = State.IDLE
                        value = v
                        indicator.translationX = 0f
                        requestLayout()
                    }
                    .start()
            } else {
                value = v
                requestLayout()
            }
        }
    }

    private fun findOption(value: Type) = when (value) {
        Type.EDITOR_CHOICE -> editorChoice
        Type.HOTTEST -> hottest
        Type.LATEST -> latest
    }

    private fun onValueChanged(new: Type) {
        allOptions.forEach { it.isSelected = false }
        findOption(new).isSelected = true
        onValueChanged.invoke(new)
    }
}

class OptionTextView: AppCompatTextView {

    companion object {
        private const val TAG = "OptionTextView"
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        isSelected = false
        setPadding(intArrayOf(8, 5, 8, 5))
    }

    fun setType(type: CommentSwitch.Type) {
        setText(type.text)
    }

    override fun dispatchSetSelected(selected: Boolean) {
        super.dispatchSetSelected(selected)
        if (selected) {
            setTextColor(context.getColorCompat(R.color.base_3a))
            typeface = Typeface.DEFAULT_BOLD
        } else {
            setTextColor(context.getColorCompat(R.color.base_7d))
            typeface = Typeface.DEFAULT
        }
    }
}