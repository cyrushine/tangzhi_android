package com.ifanr.android.tangzhi.ui.product.posts

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView
import com.ifanr.android.tangzhi.model.Product
import com.ifanr.android.tangzhi.ui.base.BaseFlatEpoxyRecyclerView

/**
 * 评测怎么看
 */
class PostList: BaseFlatEpoxyRecyclerView {

    private val controller = PostController()

    constructor(context: Context) : super(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setController(controller)
    }

    fun setPosts(posts: List<Product.CachedPost>, total: Long) {
        controller.setPosts(posts, total)
    }
}