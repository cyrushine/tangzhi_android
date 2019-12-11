package com.ifanr.android.tangzhi.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ifanr.android.tangzhi.R
import com.ifanr.android.tangzhi.ext.dp2px

class Toolbar: ConstraintLayout {

    val titleTv: TextView
    val close: View

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        LayoutInflater.from(context).inflate(R.layout.app_toolbar, this, true)
        titleTv = findViewById(R.id.toolbarTitleTv)
        close = findViewById(R.id.toolbarCloseBtn)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(context.dp2px(44), MeasureSpec.EXACTLY))
    }
}