package com.ifanr.tangzhi.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.Event
import com.ifanr.tangzhi.EventBus
import com.ifanr.tangzhi.model.Comment
import com.ifanr.tangzhi.model.Page
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.model.ProductList
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProductViewModel @Inject constructor (
    private val repository: BaasRepository,
    private val eventBus: EventBus
): BaseViewModel() {

    companion object {
        private const val PRODUCT_LIST_MAX = 6      // 产品清单最多显示 6 条
    }

    val product = MutableLiveData<Product>()
    // 已发布的点评
    val review = MutableLiveData<Comment>()
    val errorOnLoad = MutableLiveData<Throwable>()

    /**
     * > 0，delay loading
     * = 0，loading
     * < 0，dismiss loading
     */
    val loading = MutableLiveData<Int>()
    val toast = MutableLiveData<String>()

    /**
     * 相关产品列表
     * total == 0，隐藏「相关产品」章节
     * total > 0
     *      empty list - 加载中
     *      list       - 显示产品列表
     */
    val relatedProducts: LiveData<RelatedProducts> = Transformations.switchMap(product) { product ->
        val ids = product.similarProduct
        val liveData = MutableLiveData<RelatedProducts>()
        liveData.value = RelatedProducts(products = emptyList(), total = 0, productId = "")

        if (ids.isNotEmpty()) {
            repository.relatedProducts(ids)
                .subscribeOn(Schedulers.io())
                .subscribe(Consumer {
                    liveData.postValue(RelatedProducts(
                        products = it.take(PRODUCT_LIST_MAX),
                        total = it.size,
                        productId = product.id))
                })
        }
        liveData
    }

    /**
     * 产品清单列表
     *
     */
    val productList = MutableLiveData<Page<ProductList>>()

    val paramVisiable: LiveData<Boolean> = Transformations.map(product) { product ->
        product.highlightParamVisible || product.paramVisible
    }

    // 关注
    val isFollowed = MutableLiveData<Boolean>().apply { value = false }

    init {

        // 清单
        product.observeForever {
            val id = it?.id
            if (!id.isNullOrEmpty()) {
                repository.getProductListByProductId(id, pageSize = PRODUCT_LIST_MAX)
                    .subscribeOn(Schedulers.io())
                    .autoDispose(this)
                    .subscribe(Consumer { productList.postValue(it) })
            }
        }

        // 关注状态
        product.observeForever { refreshIsFollowed() }
        eventBus.subscribe(this, Consumer {
            when (it) {
                is Event.SignIn, is Event.SignOut -> refreshIsFollowed()
                is Event.ReviewCreated -> onReviewChanged(it.review)
                is Event.ReviewChanged -> onReviewChanged(it.review)
            }
        })

        // 发布的点评
        product.observeForever { it?.also {
            if (repository.signedIn()) {
                repository.myProductReview(it.id)
                    .subscribeOn(Schedulers.io())
                    .autoDispose(this)
                    .subscribe(Consumer { review.postValue(it) })
            }
        }}
    }

    fun load(productId: String) {
        repository.getProductById(productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(this)
            .subscribe({ product.value = it }, { errorOnLoad.value = it })
    }

    // 「关注」按钮被点击
    fun onFollowClick() {
        if (!repository.signedIn()) {
            ARouter.getInstance().build(Routes.signInByWechat).navigation()
            return
        }

        val productId = product.value?.id
        if (!productId.isNullOrEmpty()) {
            val followed = isFollowed.value == true
            val request = if (followed) repository.unFollowProduct(productId) else
                repository.followProduct(productId)
            request.subscribeOn(Schedulers.io())
                .doOnSubscribe { loading.postValue(1) }
                .doAfterTerminate { loading.postValue(-1) }
                .autoDispose(this)
                .subscribe({
                    isFollowed.postValue(!followed)
                }, {
                    toast.postValue(it.message)
                })
        }
    }

    // 刷新关注状态
    private fun refreshIsFollowed() {
        val productId = product.value?.id
        if (!repository.signedIn() || productId.isNullOrEmpty()) {
            isFollowed.value = false
            return
        }

        repository.isProductFollowed(productId)
            .subscribeOn(Schedulers.io())
            .autoDispose(this)
            .subscribe(Consumer { isFollowed.postValue(it) })
    }

    // 监听点评改变事件
    private fun onReviewChanged(income: Comment) {
        if (income.productId == product.value?.id) {
            review.value = income
        }
    }

}

data class RelatedProducts(
    val productId: String,
    val products: List<Product>,
    val total: Int
)