package com.ifanr.tangzhi.ui.search.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getColorCompat
import com.ifanr.tangzhi.ext.inflateInto
import com.ifanr.tangzhi.ui.widgets.RoundedRectDrawable

class SearchBar: ConstraintLayout {

    interface Listener {
        fun onTextChanged(text: String) {}
        fun onSearchClick(text: String) {}
    }

    private val input: EditText
    private val clear: View

    var listener: Listener = object: Listener {}

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    init {
        inflateInto(R.layout.app_search_bar)
        background = RoundedRectDrawable(color = context.getColorCompat(R.color.base_f4))
        input = findViewById(R.id.searchBarInput)
        clear = findViewById(R.id.searchBarClear)

        clear.setOnClickListener {
            input.text.clear()
        }

        input.addTextChangedListener {
            val text = it?.toString() ?: ""
            clear.visibility = if (text.isEmpty()) View.INVISIBLE else View.VISIBLE
            listener.onTextChanged(text)
        }

        input.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    listener.onSearchClick(input.text.toString())
                    true
                }
                else -> false
            }
        }
    }

    fun setText(text: CharSequence) {
        input.setText(text)
    }
}