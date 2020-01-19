package com.ifanr.tangzhi.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getColorCompat
import com.ifanr.tangzhi.ext.inflateInto

/**
 * 点赞按钮
 */
class UpView: ConstraintLayout {

    private val countTv: TextView
    private val icon: View

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        inflateInto(R.layout.review_up_view)
        countTv = findViewById(R.id.countTv)
        icon = findViewById(R.id.icon)
        isSelected = false
        clipChildren = false
        clipToPadding = false
    }

    fun setCount(count: Int) {
        countTv.text = if (count <= 0) context.getText(R.string.object_up)
        else if (count > 999) "999+"
        else count.toString()
    }

    override fun dispatchSetSelected(selected: Boolean) {
        super.dispatchSetSelected(selected)
        countTv.setTextColor(context.getColorCompat(
            if (selected) R.color.base_red else R.color.base_3a))
    }

    override fun performClick(): Boolean {
        icon.animate().scaleX(1.4f).scaleY(1.4f).rotation(-15f).setDuration(150)
            .withEndAction {
                icon.animate().scaleX(1f).scaleY(1f).rotation(0f).setDuration(150)
                    .start()
            }
            .start()
        return super.performClick()
    }
}