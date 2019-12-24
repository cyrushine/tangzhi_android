package com.ifanr.tangzhi.ui.product.list

import android.util.Log
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.OnModelClickListener
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.model.Page
import com.ifanr.tangzhi.model.ProductList
import com.ifanr.tangzhi.ui.base.BaseEpoxyController
import com.ifanr.tangzhi.ui.base.BaseTypedController
import com.ifanr.tangzhi.ui.product.widgets.SectionHeaderView
import com.ifanr.tangzhi.ui.product.widgets.SectionHeaderViewModel_
import com.minapp.android.sdk.util.PagedList
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject

class ProductListController (
    val onHeaderClickListener: OnModelClickListener<SectionHeaderViewModel_, SectionHeaderView>
): BaseTypedController<Page<ProductList>>() {

    companion object {
        private const val TAG = "ProductListController"
    }

    @AutoModel
    lateinit var header: SectionHeaderViewModel_

    override fun buildModels(data: Page<ProductList>?) {
        val total = data?.total ?: 0
        val list = data?.data ?: emptyList()

        if (list.isNotEmpty() && total > 0) {
            with(header) {
                title(R.string.product_list_title)
                count(R.string.product_list_size, total)
                listener(onHeaderClickListener)
                addTo(this@ProductListController)
            }

            list.forEach {
                productList {
                    id(it.id)
                    data(it)
                    style(ProductListModel.Style.PRODUCT_PAGE)
                }
            }
        }
    }

}