package com.ifanr.tangzhi.ui.comment.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.StringRes
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ui.icons.IconView
import kotlin.properties.Delegates

class LoadMoreTextView: IconView {

    enum class State (@StringRes val text: Int) {
        IDLE(R.string.comment_child_load_more),
        LOADING(R.string.loading)
    }

    var state by Delegates.observable(State.IDLE) { _, _, new ->
        setText(new.text)
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        state = State.IDLE
    }
}