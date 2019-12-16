package com.ifanr.tangzhi.ui.product.posts

import android.content.Context
import android.util.AttributeSet
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.Routes
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.ui.base.FlatVerticalEpoxyRV
import com.ifanr.tangzhi.ui.product.widgets.SectionHeaderView
import io.reactivex.functions.Consumer

/**
 * 评测怎么看
 */
class PostList: FlatVerticalEpoxyRV {

    private val controller = PostController(consumer = Consumer {
        when (it) {
            SectionHeaderView.Event.OnCountClick -> {
                ARouter.getInstance().build(Routes.postList)
                    .withString(Routes.postListProductId, productId)
                    .navigation(context)
            }
        }
    })

    private var productId: String = ""

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

    fun setData(productId: String, posts: List<Product.CachedPost>, total: Int) {
        this.productId = productId
        controller.setData(posts.take(5), total)
    }
}