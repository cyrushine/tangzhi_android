package com.ifanr.tangzhi.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.Event
import com.ifanr.tangzhi.EventBus
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.route.Routes
import dagger.android.DaggerIntentService
import io.reactivex.functions.Consumer
import javax.inject.Inject

/**
 * 检查账号
 */
class ProfileService : DaggerIntentService("ProfileService") {

    companion object {
        private const val TAG = "ProfileService"

        fun start(ctx: Context) {
            ctx.startService(Intent(ctx, ProfileService::class.java))
        }
    }

    @Inject
    lateinit var repository: BaasRepository

    @Inject
    lateinit var bus: EventBus

    override fun onCreate() {
        super.onCreate()
        bus.subscribe(onNext = Consumer {
            when (it) {
                Event.SignIn -> start(applicationContext)
            }
        })
    }

    override fun onHandleIntent(intent: Intent?) {
        // 弹出绑定手机页面
        if (repository.signedIn()) {
            try {
                val user = repository.currentUser().blockingGet()
                if (user.phone.isNullOrEmpty()) {
                    ARouter.getInstance().build(Routes.bindLocalPhone).navigation(applicationContext)
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message, e)
            }
        }
    }
}
