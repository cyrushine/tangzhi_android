package com.ifanr.tangzhi.ui.product.comments.review.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.inflateInto
import com.ifanr.tangzhi.ui.widgets.ProductTag
import com.ifanr.tangzhi.ui.widgets.ProductTagList

/**
 * 「大家都说」卡片
 */
class TagCard: ConstraintLayout {

    var openTagDialog: () -> Unit = {}

    private val list: ProductTagList

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setBackgroundResource(R.drawable.product_review_tag_bg)
        inflateInto(R.layout.review_tag_card)
        findViewById<View>(R.id.openBtn).setOnClickListener { openTagDialog.invoke() }
        list = findViewById(R.id.list)
    }

    fun setData(data: List<ProductTag>) {
        list.setData(data)
    }
}