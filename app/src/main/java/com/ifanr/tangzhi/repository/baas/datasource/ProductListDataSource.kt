package com.ifanr.tangzhi.repository.baas.datasource

import com.ifanr.tangzhi.model.BaseModel
import com.ifanr.tangzhi.model.ProductList
import com.ifanr.tangzhi.repository.baas.Tables
import com.minapp.android.sdk.database.query.Where

class ProductListDataSource (productId: String): BaseDataSource<ProductList>(
    table = Tables.itemList,
    clz = ProductList::class.java,
    initQuery = {
        val where = Where()
            .equalTo(ProductList.COL_STATUS, BaseModel.STATUS_APPROVED)
            .containedIn(ProductList.COL_ITEMS, listOf(productId))
        put(where)
    }
)