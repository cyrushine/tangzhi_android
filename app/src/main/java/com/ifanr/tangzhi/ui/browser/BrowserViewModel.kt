package com.ifanr.tangzhi.ui.browser

import android.content.Context
import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.ifanr.tangzhi.BuildConfig
import com.ifanr.tangzhi.ext.appName
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import io.reactivex.Single
import javax.inject.Inject

class BrowserViewModel @Inject constructor(
    private val repository: BaasRepository,
    private val ctx: Context
): BaseViewModel() {

    /**
     * 只用来保存当前页面的地址，不导致 web view 加载
     */
    val url = MutableLiveData<String>()

    val title = MutableLiveData<String>()

    /**
     * 0 - 100
     */
    val progress = MutableLiveData<Int>()

    fun buildFeedbackData(): Single<String> = Single.fromCallable {
        var userId: String? = null
        var nickname: String? = null
        var avatar: String? = null

        if (repository.signedIn()) {
            try {
                val user = repository.currentUser().blockingGet()
                userId = user.id.toString()
                nickname = user.displayName
                avatar = user.displayAvatar
            } catch (e: Exception) {}
        }

        FeedbackParams(
            userId = userId,
            userNickname = nickname,
            userAvatar = avatar,
            clientInfo = "${ctx.appName} android",
            clientVersion = BuildConfig.VERSION_NAME,
            os = Build.FINGERPRINT,
            osVersion = Build.VERSION.SDK_INT.toString()
        ).toString()
    }
}

private data class FeedbackParams(
    val userId: String?,
    val userNickname: String?,
    val userAvatar: String?,
    val clientInfo: String?,
    val clientVersion: String?,
    val os: String?,
    val osVersion: String?
) {
    override fun toString(): String {
        val list = ArrayList<String>(7)
        if (!userId.isNullOrEmpty())
            list.add("openid=$userId")
        if (!userNickname.isNullOrEmpty())
            list.add("nickname=$userNickname")
        if (!userAvatar.isNullOrEmpty())
            list.add("avatar=$userAvatar")
        if (!clientInfo.isNullOrEmpty())
            list.add("clientInfo=$clientInfo")
        if (!clientVersion.isNullOrEmpty())
            list.add("clientVersion=$clientVersion")
        if (!os.isNullOrEmpty())
            list.add("os=$os")
        if (!osVersion.isNullOrEmpty())
            list.add("osVersion=$osVersion")
        return list.joinToString(separator = "&")
    }
}