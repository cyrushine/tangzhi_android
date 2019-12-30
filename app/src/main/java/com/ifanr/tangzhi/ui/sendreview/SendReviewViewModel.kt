package com.ifanr.tangzhi.ui.sendreview

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.filter.Filter
import com.zhihu.matisse.internal.entity.IncapableCause
import com.zhihu.matisse.internal.entity.Item
import javax.inject.Inject

class SendReviewViewModel @Inject constructor(): BaseViewModel() {

    val productId = MutableLiveData<String>()
    val productName = MutableLiveData<String>()
    val rating = MutableLiveData<Int>()
    val comment = MutableLiveData<String>()

    // 图片 file:// 路径，用来上传
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
    }

    /**
     * 发表点评
     */
    fun sendReview() {

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