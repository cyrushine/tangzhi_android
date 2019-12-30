package com.ifanr.tangzhi.ui.product.comments.review

import android.util.Log
import androidx.lifecycle.*
import com.ifanr.tangzhi.model.Comment
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.repository.baas.BaaSRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import com.ifanr.tangzhi.ui.widgets.CommentSwitch
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import kotlin.random.Random

class ReviewViewModel @Inject constructor (
    private val repository: BaaSRepository
) : BaseViewModel() {

    companion object {
        private const val TAG = "ReviewViewModel"
    }

    enum class State {
        IDLE, LOADING
    }

    // 产品信息
    val product = MutableLiveData<Product>()
    // 标签列表
    val tags = MediatorLiveData<List<Comment>>()
    // 点评列表, boolean == true 表示 scroll to top
    val reviews = MediatorLiveData<Pair<List<Comment>, Boolean>>()
    // 点评总数
    val reviewCount = MutableLiveData<Int>()
    // 排序
    val orderBy = MutableLiveData<CommentSwitch.Type>()

    // 下一页的页码
    private var page = 0
    private var state = State.IDLE
    // 列表是否结束
    private var end = false

    init {
        tags.addSource(product) {
            it?.id?.also {
                repository.loadAllTags(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .autoDispose(this)
                    .subscribe(Consumer { tags.value = it })
            }
        }
        reviews.addSource(product) {
            page = 0
            tryLoadReviews()
        }
        reviews.addSource(orderBy) {
            end = false
            page = 0
            tryLoadReviews()
        }
    }

    /**
     * 触发加载下一页
     */
    fun tryLoadNextPage() {
        tryLoadReviews(page + 1)
    }

    private fun tryLoadReviews(page: Int = 0) {
        val productId = product.value?.id
        val orderBy = this.orderBy.value
        if (state == State.IDLE && !end && !productId.isNullOrEmpty() && orderBy != null) {
            repository.loadPagedReviews(productId = productId, page = page, orderBy = orderBy)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { state = State.LOADING }
                .doAfterTerminate { state = State.IDLE }
                .autoDispose(this)
                .subscribe(Consumer {
                    /*val random = Random(System.currentTimeMillis())
                    it.data.forEach {
                        it.images = listOf(
                            "https://s3.ifanr.com/wp-content/uploads/2019/12/88888.jpg!720",
                            "https://s3.ifanr.com/wp-content/uploads/2019/12/11111-2.png!720",
                            "https://s3.ifanr.com/wp-content/uploads/2019/12/89-2.png!720",
                            "https://s3.ifanr.com/wp-content/uploads/2019/12/Graphenephene.jpg!720",
                            "https://s3.ifanr.com/wp-content/uploads/2019/12/1576867506433-12_19_2019_LUXURY_EGG_FREEZING_CV.jpeg!720",
                            "https://s3.ifanr.com/wp-content/uploads/2019/12/pic31_l.jpg!720",
                            "https://s3.ifanr.com/wp-content/uploads/2019/12/er.png!720",
                            "https://s3.ifanr.com/wp-content/uploads/2019/12/titu_meitu_1-1.jpg!720",
                            "https://media.ifanrusercontent.com/user_files/wpdata/images/11/c9/11c9c9332d289471c98f68cbefa6b27899ded4f5-5d2c15f2d2c8d6d72ea84bc13af339a94f4de88b.png",
                            "https://s3.ifanr.com/wp-content/uploads/2019/10/3611a5ba-fd5a-4eca-8a09-f4566dd24c2b.png!720",
                            "https://s3.ifanr.com/wp-content/uploads/2019/12/Transpo-Tesla_19256007023034-c.jpg!720"
                        ).take(random.nextInt(10))
                    }*/

                    if (page == 0) {
                        reviews.value = it.data to true
                    } else {
                        val previous = reviews.value?.first
                        val data = if (previous != null) previous + it.data else it.data
                        reviews.value = data to false
                    }
                    if (it.total > reviewCount.value ?: 0)
                        reviewCount.value = it.total

                    if (it.data.size < it.pageSize) {
                        end = true
                    } else {
                        end = false
                        this.page = page
                    }
                })
        }
    }
}
