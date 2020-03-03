package com.ifanr.tangzhi.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.finishDelay
import com.ifanr.tangzhi.ext.toast
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseActivity

/**
 * 处理 tangzhi://
 */
class SchemeHandlerActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "SchemeHandler"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tang_zhi_scheme_handler)
        statusBar(whiteText = false)
        processData(intent.data)
    }

    private fun processData(data: Uri?) {
        Log.d(TAG, "data: $data")
        if (data != null) {
            val paths = data.pathSegments ?: listOf()
            val module = paths.firstOrNull()
            if (!module.isNullOrEmpty()) {
                val routePath = Uri.Builder()
                    .path("/${Routes.DEFAULT_GROUP}/$module")
                    .encodedQuery(data.encodedQuery)
                    .build()
                Log.d(TAG, "route path: $routePath")
                ARouter.getInstance().build(routePath).navigation(this)
                finishDelay()
                return
            }
        }

        finish()
    }

    override fun onBackPressed() {}
}
