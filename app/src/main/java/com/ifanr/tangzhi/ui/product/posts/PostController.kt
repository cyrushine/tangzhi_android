package com.ifanr.tangzhi.ui.product.posts

import android.view.View
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.OnModelClickListener
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.ui.base.model.BaseTyped2Controller
import com.ifanr.tangzhi.ui.product.widgets.SectionHeaderView
import com.ifanr.tangzhi.ui.product.widgets.SectionHeaderViewModel_

class PostController (
    val onHeaderClickListener: OnModelClickListener<SectionHeaderViewModel_, SectionHeaderView>
): BaseTyped2Controller<List<Product.CachedPost>, Int>() {

    companion object {
        private const val MAX_POST = 4
    }

    @AutoModel
    lateinit var header: SectionHeaderViewModel_

    private val emptyHeaderClick: OnModelClickListener<SectionHeaderViewModel_, SectionHeaderView> =
        OnModelClickListener { _, _, _, _ -> }

    override fun buildModels(posts: List<Product.CachedPost>?, total: Int?) {
        if (posts?.isNotEmpty() == true) {
            val count = total ?: 0
            with(header) {
                listener(if (count > MAX_POST) onHeaderClickListener else emptyHeaderClick)
                title(R.string.product_posts)
                count(R.string.product_posts_total, count)
                countVisible(count > MAX_POST)
                addTo(this@PostController)
            }
        }

        posts?.take(MAX_POST)?.forEach {
            post {
                data(it)
                id(it.id)
            }
        }
    }

    override fun setData(posts: List<Product.CachedPost>, total: Int) {
        super.setData(posts, total)
    }

}