package com.ifanr.tangzhi.ext

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.bumptech.glide.Glide
import com.ifanr.tangzhi.Const
import com.ifanr.tangzhi.R
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject
import com.tencent.mm.opensdk.openapi.IWXAPI
import java.io.ByteArrayOutputStream
import kotlin.math.ceil

// 分享小程序时，缩略图的最大容量：32KB（参考 WXMediaMessage.checkArgs()）
private const val THUMB_MAX_SIZE = (30 * 1024).toDouble()

/**
 * 把 [Bitmap] 压缩为 jpeg byte array
 * 适用于微信分享的 thumb，当超过 [THUMB_MAX_SIZE] 时通过 inSampleSize 缩小图片
 */
fun WXMediaMessage.setCompressedThumbImage(bm: Bitmap) {
    var bytes = ByteArrayOutputStream().use {
        bm.compress(Bitmap.CompressFormat.JPEG, 85, it)
        it.toByteArray()
    }

    var loop = 3
    while (bytes.size > THUMB_MAX_SIZE && loop > 0) {
        val options = BitmapFactory.Options().apply {
            inSampleSize = ceil(bytes.size / THUMB_MAX_SIZE).toInt()
        }
        val tmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)

        bytes = ByteArrayOutputStream().use {
            tmp.compress(Bitmap.CompressFormat.JPEG, 85, it)
            it.toByteArray()
        }

        try { tmp.recycle() } catch (e: Exception) {}
        loop--
    }

    thumbData = bytes
}

/**
 * 分享产品至糖纸小程序
 */
fun IWXAPI.shareProductByMinProgram(id: String, coverImage: Bitmap, title: String) {
    val req = SendMessageToWX.Req().apply {
        scene = SendMessageToWX.Req.WXSceneSession
        message = WXMediaMessage().apply {
            mediaObject = WXMiniProgramObject().apply {
                userName = Const.TANGZHI_USER_NAME
                path = "${Const.TANGZHI_PRODUCT_PATH}${id}"
                webpageUrl = path
            }
            setCompressedThumbImage(coverImage)
            this.title = title
        }
    }
    sendReq(req)
}