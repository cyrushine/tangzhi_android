package com.ifanr.tangzhi.route

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.App
import com.ifanr.tangzhi.exceptions.NeedSignInException
import com.ifanr.tangzhi.repository.baas.BaasRepository
import javax.inject.Inject

@Interceptor(priority = 8, name = "登录拦截器")
class SignInInterceptor: IInterceptor {

    @Inject
    lateinit var repository: BaasRepository

    override fun process(postcard: Postcard, callback: InterceptorCallback) {
        if (postcard.extra.and(Extra.signIn) == Extra.signIn && !repository.signedIn()) {
            callback.onInterrupt(NeedSignInException())
            ARouter.getInstance().build(Routes.signInByWechat).navigation()
        } else {
            callback.onContinue(postcard)
        }
    }

    override fun init(context: Context) {
        (context as App).androidInjector().inject(this)
    }

}