package com.ifanr.android.tangzhi.ui.product.posts

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.EpoxyController
import com.ifanr.android.tangzhi.model.Product

class PostController: EpoxyController() {

    private var posts = emptyList<Product.CachedPost>()
    private var total = 0L

    @AutoModel
    lateinit var header: PostHeaderModel_

    override fun buildModels() {
        if (posts.isNotEmpty()) {
            header.total = total
            add(header)
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
        this.total = total
        requestModelBuild()
    }

}