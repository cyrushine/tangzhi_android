package com.ifanr.android.tangzhi.ui.product.posts

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.BaseEpoxyAdapter
import com.airbnb.epoxy.EpoxyController
import com.ifanr.android.tangzhi.R
import com.ifanr.android.tangzhi.model.Product
import com.ifanr.android.tangzhi.ui.base.BaseEpoxyController
import com.ifanr.android.tangzhi.ui.product.widgets.SectionHeaderViewModel_

class PostController: BaseEpoxyController() {

    private var posts = emptyList<Product.CachedPost>()
    private var totalCount = 0L

    @AutoModel
    lateinit var header: SectionHeaderViewModel_

    override fun buildModels() {
        if (posts.isNotEmpty()) {
            with(header) {
                title(R.string.product_posts)
                count(R.string.product_posts_total, totalCount)
                addTo(this@PostController)
            }
        }

        posts.forEach {
            PostModel_()
                .data(it)
                .id(it.id)
                .addTo(this)
        }
    }

    fun setPosts(list: List<Product.CachedPost>, total: Long) {
        posts = list
        this.totalCount = total
        requestModelBuild()
    }

}