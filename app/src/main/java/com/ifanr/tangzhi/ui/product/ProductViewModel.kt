package com.ifanr.tangzhi.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ifanr.tangzhi.Const
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.model.ProductList
import com.ifanr.tangzhi.repository.product.ProductRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import com.minapp.android.sdk.util.PagedList
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProductViewModel @Inject constructor (
    private val repository: ProductRepository
): BaseViewModel() {

    companion object {
        private const val PRODUCT_LIST_MAX = 6      // 产品清单最多显示 6 条
    }

    val product = MutableLiveData<Product>()
    val errorOnLoad = MutableLiveData<Throwable>()

    /**
     * 相关产品列表
     * total == 0，隐藏「相关产品」章节
     * total > 0
     *      empty list - 加载中
     *      list       - 显示产品列表
     */
    val relatedProducts: LiveData<RelatedProducts> = Transformations.switchMap(product) { product ->
        val ids = product.similarProduct
        val size = ids.size
        val liveData = MutableLiveData<RelatedProducts>()
        liveData.value = RelatedProducts(products = emptyList(), total = size, productId = "")

        if (size > 0) {
            repository.getProductsByIds(ids.take(PRODUCT_LIST_MAX))
                .subscribeOn(Schedulers.io())
                .subscribe(Consumer {
                    liveData.postValue(RelatedProducts(
                        products = it, total = size, productId = product.id))
                })
        }
        liveData
    }

    /**
     * 产品清单列表
     *
     */
    val productList = MutableLiveData<PagedList<ProductList>>()

    val paramVisiable: LiveData<Boolean> = Transformations.map(product) { product ->
        product.highlightParamVisible || product.paramVisible
    }

    init {
        product.observeForever {
            val id = it.id
            if (id.isNotEmpty()) {
                repository.getProductListByProductId(id, pageSize = PRODUCT_LIST_MAX)
                    .subscribeOn(Schedulers.io())
                    .autoDispose(this)
                    .subscribe(Consumer { productList.postValue(it) })
            }
        }
    }

    fun load(productId: String) {
        repository.getProductById(productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(this)
            .subscribe({ product.value = it }, { errorOnLoad.value = it })
    }

}

data class RelatedProducts(
    val productId: String,
    val products: List<Product>,
    val total: Int
)