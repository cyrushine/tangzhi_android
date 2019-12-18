package com.ifanr.tangzhi.ui.gallery

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlin.properties.Delegates

class IndicatorTextView: AppCompatTextView {

    // start from 0
    private var total: Int by Delegates.observable(0) { _, old, new ->
        if (old != new) {
            onDataChanged()
        }
    }

    private var index: Int by Delegates.observable(0) { _, old, new ->
        if (old != new) {
            onDataChanged()
        }
    }

    private val onPageChanged = object: ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            index = position
        }
    }

    private val dataObserver = object: RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            total = adapter?.itemCount ?: total
        }
    }

    private var adapter: RecyclerView.Adapter<*>? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        setTextColor(Color.WHITE)
    }

    fun bind(vp: ViewPager2) {
        vp.registerOnPageChangeCallback(onPageChanged)
        adapter = vp.adapter
        adapter?.registerAdapterDataObserver(dataObserver)
        total = adapter?.itemCount ?: 0
    }

    @SuppressLint("SetTextI18n")
    private fun onDataChanged() {
        text = "${if (total == 0) 0 else index + 1}/$total"
    }
}