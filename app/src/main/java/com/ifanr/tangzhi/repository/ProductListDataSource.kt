package com.ifanr.tangzhi.repository

import com.ifanr.tangzhi.model.BaseModel
import com.ifanr.tangzhi.model.ProductList
import com.ifanr.tangzhi.ui.base.BaseDataSource
import com.minapp.android.sdk.database.query.Where

class ProductListDataSource (productId: String): BaseDataSource<ProductList>(
    table = itemList,
    clz = ProductList::class.java,
    initQuery = {
        val where = Where()
            .equalTo(ProductList.COL_STATUS, BaseModel.STATUS_APPROVED)
            .containedIn(ProductList.COL_ITEMS, listOf(productId))
        put(where)
    }
)