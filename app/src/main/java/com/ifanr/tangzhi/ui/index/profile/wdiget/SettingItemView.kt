package com.ifanr.tangzhi.ui.index.profile.wdiget

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.inflateInto
import com.ifanr.tangzhi.ext.setPadding
import com.ifanr.tangzhi.ext.setSelectableItemBackground

class SettingItemView: ConstraintLayout {

    private lateinit var text: TextView
    private lateinit var icon: TextView

    constructor(context: Context) : super(context) { init(context, null) }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    { init(context, attrs) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init(context, attrs) }

    private fun init(ctx: Context, attrs: AttributeSet?) {
        inflateInto(R.layout.setting_item)
        text = findViewById(R.id.text)
        icon = findViewById(R.id.icon)
        setPadding(intArrayOf(20, 16, 20, 16))
        setSelectableItemBackground()

        if (attrs != null) {
            val ta = ctx.obtainStyledAttributes(attrs, R.styleable.SettingItemView)
            text.text = ta.getText(R.styleable.SettingItemView_settingItemViewText)
            icon.text = ta.getText(R.styleable.SettingItemView_settingItemViewIcon)
            ta.recycle()
        }
    }
}