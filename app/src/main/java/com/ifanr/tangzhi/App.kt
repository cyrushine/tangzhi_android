package com.ifanr.tangzhi

import android.graphics.Bitmap
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.ifanr.tangzhi.di.DaggerAppComponent
import com.ifanr.tangzhi.glide.RoundedBitmapDrawableTranscoder
import com.minapp.android.sdk.BaaS
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App: DaggerApplication() {

    companion object {
        private const val TAG = "App"
    }

    override fun onCreate() {
        super.onCreate()
        BaaS.init(Const.BAAS_ID, this)
        initARouter()
        initGlide()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

    private fun initARouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
            ARouter.printStackTrace()
        }
        ARouter.init(this)
    }

    private fun initGlide() {
        val glide = Glide.get(this)
        glide.registry.register(Bitmap::class.java, RoundedBitmapDrawable::class.java,
            RoundedBitmapDrawableTranscoder(this, glide.bitmapPool))
    }
}
