package com.ifanr.tangzhi.repository.baas.datasource

import com.ifanr.tangzhi.model.BaseModel
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.repository.baas.Tables
import com.minapp.android.sdk.database.Record
import com.minapp.android.sdk.database.query.Where

/**
 * TODO 使用正则表达式
 * 搜索产品
 * @param key 关键字
 */
class SearchDataSource (key: String): BaseDataSource<Product>(
    table = Tables.product,
    clz = Product::class.java,
    initQuery = {
        var where = Where.or(
            Where().apply {
                contains(Product.COL_NAME, key)
            },
            Where().apply {
                contains(Product.COL_BRIEF, key)
            })
        where = Where.or(where, Where().apply {
            contains(Product.COL_DESCRIPTION, key)
        })
        where = Where.or(where, Where().apply {
            arrayContains(Product.COL_CATEGORY, listOf(key))
        })
        where = Where.and(where, Where().apply {
            equalTo(Product.COL_TYPE, Product.TYPE_HARDWARE)
            equalTo(Product.COL_STATUS, BaseModel.STATUS_APPROVED)
        })
        put(where)
        orderBy(listOf("-${Product.COL_REVIEW_COUNT}",
            "-${Record.UPDATED_AT}",
            "-${Product.COL_PRIORITY}",
            "-${Product.COL_RATING}"))
    }
)