package com.ifanr.tangzhi.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.ext.getColorCompat

class AppToolbar: ConstraintLayout {

    val titleTv: TextView
    val close: TextView
    private val bottomDivider: View

    constructor(context: Context?) : super(context) { construct(null) }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { construct(attrs) }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { construct(attrs) }

    init {
        LayoutInflater.from(context).inflate(R.layout.app_toolbar, this, true)
        titleTv = findViewById(R.id.toolbarTitleTv)
        close = findViewById(R.id.toolbarCloseBtn)
        bottomDivider = findViewById(R.id.toolbarBottomDivider)
        bottomDivider.visibility = View.GONE
    }

    private fun construct(attrs: AttributeSet?) {
        var style = Style.WHITE
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.AppToolbar)
            style = when (ta.getInt(R.styleable.AppToolbar_appToolbarStyle, 0)) {
                0 -> Style.WHITE
                else -> Style.BLACK
            }

            ta.getString(R.styleable.AppToolbar_appToolbarTitle)?.also {
                titleTv.text = it
            }

            if (ta.getBoolean(R.styleable.AppToolbar_appToolbarBottomDivider, false)) {
                bottomDivider.visibility = View.VISIBLE
            }
            ta.recycle()
        }
        setStyle(style)
    }

    fun setStyle(style: Style) {
        titleTv.setTextColor(context.getColorCompat(style.titleColor))
        close.setTextColor(context.getColorCompat(style.iconColor))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(context.dp2px(44), MeasureSpec.EXACTLY))
    }

    enum class Style (
        @ColorRes val titleColor: Int,
        @ColorRes val iconColor: Int
    ) {
        WHITE(titleColor = R.color.black, iconColor = R.color.black),
        BLACK(titleColor = R.color.white, iconColor = R.color.white)
    }
}