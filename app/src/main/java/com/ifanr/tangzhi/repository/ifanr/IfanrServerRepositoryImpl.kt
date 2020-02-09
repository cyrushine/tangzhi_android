package com.ifanr.tangzhi.repository.ifanr

import android.util.Log
import com.ifanr.tangzhi.Event
import com.ifanr.tangzhi.EventBus
import com.ifanr.tangzhi.exceptions.SignInByEmailFailException
import com.ifanr.tangzhi.repository.ifanr.model.SignInByEmailRequest
import com.ifanr.tangzhi.repository.ifanr.model.SignInByEmailResponse
import com.minapp.android.sdk.auth.Auth
import io.reactivex.Completable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class IfanrServerRepositoryImpl @Inject constructor(
    private val bus: EventBus
): IfanrServerRepository {

    companion object {
        private const val TAG = "IfanrServerRepository"
    }

    private val client by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("http://i-v5208.eng.szx.ifanrx.com/")  // TODO，测试地址
            .build()
            .create(IfanrServer::class.java)
    }

    override fun signInByEmail(email: String, pwd: String): Completable = Completable.fromAction {
        val response = client.signInByEmail(SignInByEmailRequest(email, pwd))
            .blockingGet()

        val token = response.token
        val userId = response.userId
        val expiredIn = response.expiresIn
        if (response.status != SignInByEmailResponse.STATUS_OK ||
                token.isNullOrEmpty() ||
                userId == null ||
                expiredIn == null)
            throw SignInByEmailFailException()

        Auth.signIn(token, userId.toString(), expiredIn)
        bus.post(Event.SignIn)
    }
}