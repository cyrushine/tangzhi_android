package com.ifanr.tangzhi.ui.widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.inflateInto
import com.ifanr.tangzhi.ui.icons.IconView

class ShareButton: ConstraintLayout {

    private lateinit var icon: IconView
    private lateinit var text: TextView

    constructor(context: Context?) : super(context) { construct(null) }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { construct(attrs) }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { construct(attrs) }

    private fun construct(attrs: AttributeSet?) {
        inflateInto(R.layout.share_button)
        icon = findViewById(R.id.icon)
        text = findViewById(R.id.text)

        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.ShareButton)
            icon.text = ta.getString(R.styleable.ShareButton_shareButtonIcon)
            icon.setTextColor(ta.getColor(R.styleable.ShareButton_shareButtonColor, Color.WHITE))
            text.text = ta.getString(R.styleable.ShareButton_shareButtonText)
            ta.recycle()
        }
    }
}