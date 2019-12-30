package com.ifanr.tangzhi.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.ext.getColorCompat
import com.ifanr.tangzhi.ext.inflateInto
import kotlin.math.min

class SimpleToolBar: ConstraintLayout {

    interface Listener {
        fun onCancel() {}
        fun onSend() {}
    }

    private val cancel: View
    private val title: TextView
    private val send: TextView
    private val minToolBarHeight by lazy { context.dp2px(44) }

    var listener: Listener = object: Listener {}
    var sendButtonEnable: Boolean = false
        set(value) {
            field = value
            send.setTextColor(context.getColorCompat(
                if (value) R.color.base_red else R.color.base_a8))
        }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        inflateInto(R.layout.app_simple_toolbar)
        cancel = findViewById(R.id.toolbarCancel)
        title = findViewById(R.id.toolbarTitle)
        send = findViewById(R.id.toolbarSend)

        cancel.setOnClickListener { listener.onCancel() }
        send.setOnClickListener {
            if (sendButtonEnable) {
                listener.onSend()
            }
        }
    }

    fun setTitle(text: String) {
        title.text = text
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightSpec = heightMeasureSpec
        val mode = MeasureSpec.getMode(heightMeasureSpec)
        val size = MeasureSpec.getSize(heightMeasureSpec)
        when (mode) {
            MeasureSpec.UNSPECIFIED -> {
                heightSpec = MeasureSpec.makeMeasureSpec(minToolBarHeight, MeasureSpec.EXACTLY)
            }
            MeasureSpec.AT_MOST -> {
                heightSpec = MeasureSpec.makeMeasureSpec(min(minToolBarHeight, size), MeasureSpec.EXACTLY)
            }
            else -> {}
        }

        super.onMeasure(widthMeasureSpec, heightSpec)
    }
}