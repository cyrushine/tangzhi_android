package com.ifanr.tangzhi.repository.baas.datasource

import com.ifanr.tangzhi.model.Favorite
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.repository.baas.favoriteTable
import com.minapp.android.sdk.database.Record
import com.minapp.android.sdk.database.query.Where

/**
 * 我的关注列表
 */
class FollowsDataSource (
    private val repository: BaasRepository
): TransformerDataSource<Favorite, Product>(
    table = favoriteTable,
    clz = Favorite::class.java,
    initQuery = {
        put(Where().apply {
            equalTo(Record.CREATED_BY, repository.currentUserWithoutData()?.id)
            equalTo(Favorite.COL_TYPE, Favorite.TYPE_HARDWARE)
            equalTo(Favorite.COL_ACTION, Favorite.ACTION_FOLLOW)
        })
        orderBy("-${Record.CREATED_BY}")
    }
) {

    override fun transform(source: List<Favorite>): List<Product> {
        var products = repository.getProductsByIds(source.map { it.subjectId })
            .blockingGet()
        if (products.size != source.size) {
            products = List(source.size) { products.getOrElse(it) { Product() } }
        }
        return products
    }
}