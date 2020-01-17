package com.ifanr.tangzhi.ui.sendreview

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.model.Comment
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import com.ifanr.tangzhi.ui.widgets.bindLoading
import com.ifanr.tangzhi.util.LoadingState
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.filter.Filter
import com.zhihu.matisse.internal.entity.IncapableCause
import com.zhihu.matisse.internal.entity.Item
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SendReviewViewModel @Inject constructor(
    private val repository: BaasRepository
): BaseViewModel() {

    // 用户已发表的点评
    val existing = MediatorLiveData<Comment>()
    val productId = MutableLiveData<String>()
    val productName = MutableLiveData<String>()
    val rating = MutableLiveData<Int>()
    val comment = MutableLiveData<String>()
    val loading = MutableLiveData<LoadingState>()
    val toast = MutableLiveData<String>()
    val finish = MutableLiveData<Boolean>()

    // 图片路径，用来上传
    // 如果是 file:// 则是用户刚选择的
    // 否则是用户已上传的
    val imagePaths = MutableLiveData<List<String>>()
    // 图片 content:// 路径，用来去重
    private var imageUri: List<Uri> = emptyList()

    // 「发表」按钮是否可用
    val sendBtnEnable = MediatorLiveData<Boolean>().apply {
        value = false
    }

    // 允许上传的图片类型
    val imageMimeTypes = setOf(MimeType.JPEG, MimeType.PNG)

    // 可以选择的图片数量
    val imageSizeForPick: Int
        get() = 9 - (imagePaths.value?.size ?: 0)

    // 图片选择器的过滤器
    val imageFilter = object: Filter() {
        override fun filter(context: Context, item: Item): IncapableCause? =
            if (imageUri.contains(item.contentUri))
                IncapableCause(IncapableCause.TOAST,
                    context.getString(R.string.send_review_image_selected))
            else null

        override fun constraintTypes(): MutableSet<MimeType> =
            imageMimeTypes.toMutableSet()
    }

    init {
        sendBtnEnable.addSource(rating) {
            sendBtnEnable.value = (it ?: 0) >= 20
        }

        // 查找用户是否对此产品发表过点评，有的话填入 form 中
        existing.addSource(productId) { it?.also {
            repository.myProductReview(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .bindLoading(loading)
                .subscribe(Consumer {
                    existing.value = it
                    imagePaths.value = it.images
                })
        }}
    }

    /**
     * 发表点评
     */
    fun sendReview() {
        if (sendBtnEnable.value == true) {
            repository.sendReview(
                productId = productId.value ?: "",
                productName = productName.value ?: "",
                content = comment.value ?: "",
                rating = rating.value?.div(10f) ?: 0f,
                images = imagePaths.value ?: emptyList())

                // 太快的话最新点评列表里刷新不出来
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .bindLoading(loading)
                .autoDispose(this)
                .subscribe({
                    toast.value = "发表评论成功"
                    Completable.timer(500, TimeUnit.MILLISECONDS)
                        .autoDispose(this)
                        .subscribe { finish.postValue(true) }
                }, { toast.value = it.message })
        }
    }

    /**
     * 添加图片
     */
    fun addImage(
        paths: List<String>,
        uris: List<Uri>
    ) {
        val list = imagePaths.value
        imagePaths.value = (if (list.isNullOrEmpty()) paths else list + paths).take(9)
        imageUri = imageUri + uris
    }

    /**
     * 移除一张图片
     */
    fun removeImage(position: Int) {
        val list = imagePaths.value
        if (!list.isNullOrEmpty()) {
            imagePaths.value = list.filterIndexed { index, _ -> index != position }
        }
        imageUri = imageUri.filterIndexed { index, _ -> index != position }
    }


}