package com.ifanr.tangzhi.ui

import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseActivity
import com.minapp.android.sdk.auth.Auth
import kotlinx.android.synthetic.main.activity_launch.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LaunchActivity : BaseActivity() {

    companion object {
        private const val TAG = "LaunchActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        /*ARouter.getInstance().build(Routes.product)
            .withString(Routes.productId, "5db8518a6f40dd69b311ffc1")
            .navigation(this)*/
        /*ARouter.getInstance().build(Routes.index).navigation(this)*/
        ARouter.getInstance().build(Routes.signInByPhone).navigation(this)

        alloc.setOnClickListener {
            ARouter.getInstance().build(Routes.share)
                .navigation(this)
        }

        GlobalScope.launch { longestPalindrome("babad") }
    }

    fun longestPalindrome(s: String): String {
        if (s.length <= 1)
            return s

        val n = s.length
        val dp = Array(n) { Array(n) { false } }

        var start = 0
        var maxLength = 0
        (0 until n).reversed().forEach { i ->
            (0 until n).forEach { j ->
                if (i <= j) {
                    val length = j - i + 1
                    dp[i][j] = when (length) {
                        1 -> true
                        2, 3 -> s[i] == s[j]
                        else -> (s[i] == s[j]) && (dp[i + 1][j - 1])
                    }
                    if (dp[i][j] && length > maxLength) {
                        start = i
                        maxLength = length
                    }
                }
            }
        }

        val lastIndex = s.lastIndex
        for (i in lastIndex downTo 0) {
            for (j in i..lastIndex) {

            }
        }
        return s.substring(start, start + maxLength)
    }
}
