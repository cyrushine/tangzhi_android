package com.ifanr.tangzhi.ui.gallery

import android.app.Application
import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.RequestManager
import com.ifanr.tangzhi.AppConfig
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.lang.Exception
import javax.inject.Inject

private const val TAG = "GalleryViewModel"

class GalleryViewModel @Inject constructor(
    private val requestManager: RequestManager,
    private val config: AppConfig
): BaseViewModel() {

    val images = MutableLiveData<ArrayList<String>>()
    val position = MutableLiveData<Int>()

    /**
     * string 下载成功的文件路径
     * exception 下载失败抛出的异常
     */
    val downloadMessage = MutableLiveData<Pair<String?, Throwable?>>()

    /**
     * 需要写权限
     */
    fun downloadImageInPosition() {
        val url = images.value?.getOrNull(position.value ?: 0)
        if (!url.isNullOrBlank()) {
            DownloadTask(requestManager, url, config)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDispose(this)
                .subscribe({ downloadMessage.value = it to null },
                    { downloadMessage.value = null to it })
        }
    }
}

private fun DownloadTask(requestManager: RequestManager, input: String, config: AppConfig) =
    Single.fromCallable {
        try {
            val file = requestManager.download(input).submit().get()
            file.copyTo(
                target = File(config.imageFolderInDCIM, File(input).name),
                overwrite = true
            ).absolutePath
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
            throw e
        }
    }