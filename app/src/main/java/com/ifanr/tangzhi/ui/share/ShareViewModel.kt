package com.ifanr.tangzhi.ui.share

import com.bumptech.glide.RequestManager
import com.ifanr.tangzhi.ext.shareProductByMinProgram
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import com.tencent.mm.opensdk.openapi.IWXAPI
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Provider

class ShareViewModel @Inject constructor(
    private val wxapi: Provider<IWXAPI>,
    private val requestManager: RequestManager
): BaseViewModel() {

    /**
     * 分享产品至微信
     */
    fun shareProductToWechat(req: ShareProductReq) {
        Completable.fromAction {
            wxapi.get().shareProductByMinProgram(
                id = req.id,
                coverImage = requestManager.asBitmap().load(req.coverImage).submit().get(),
                title = req.title)}
            .subscribeOn(Schedulers.io())
            .autoDispose(this)
            .subscribe()
    }

}