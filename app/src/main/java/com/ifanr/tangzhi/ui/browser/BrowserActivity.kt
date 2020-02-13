package com.ifanr.tangzhi.ui.browser

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.ifanr.tangzhi.Const
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ext.appLabelString
import com.ifanr.tangzhi.ext.ioTask
import com.ifanr.tangzhi.ext.startActivityForResultSafely
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.statusBar
import com.ifanr.tangzhi.ui.widgets.AppWebView
import com.ifanr.tangzhi.ui.widgets.dismissLoading
import com.ifanr.tangzhi.ui.widgets.showLoading
import com.uber.autodispose.android.lifecycle.autoDispose
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_browser.*
import kotlinx.android.synthetic.main.activity_product_param.toolbar

@Route(path = Routes.browser)
class BrowserActivity : BaseViewModelActivity() {

    companion object {
        private const val TAG = "BrowserActivity"
        private const val CHOOSE_FILE = 99
    }

    @Autowired(name = Routes.browserUrl)
    @JvmField
    var url = ""

    @Autowired(name = Routes.browserTitle)
    @JvmField
    var title = ""

    @Autowired(name = Routes.browserTitleNotChanged, required = false)
    @JvmField
    var titleNotChanged: Boolean? = false

    private var chooseFileEvent: AppWebView.Event.ShowFileChooser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        title = appLabelString()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)
        statusBar(whiteText = false)

        val vm: BrowserViewModel = viewModel()
        vm.title.observe(this, Observer {
            toolbar.titleTv.text = it
        })
        vm.title.value = title
        vm.url.value = url

        initWebview(vm)
        toolbar.close.setOnClickListener { finish() }

        if (url == Const.feedback) {
            vm.buildFeedbackData()
                .ioTask(activity = this)
                .subscribe({
                    webview.postUrl(url, it.toByteArray())
                }, {
                    Log.e(TAG, it.message, it)
                    webview.loadUrl(url)
                })

        } else {
            webview.loadUrl(url)
        }
    }

    private fun initWebview(vm: BrowserViewModel) {
        webview.bind(this)
        webview.events.autoDispose(this)
            .subscribe { when (it) {
                is AppWebView.Event.WebViewCrash -> finish()
                is AppWebView.Event.UrlChanged -> vm.url.value = it.url
                is AppWebView.Event.TitleChanged -> {
                    if (titleNotChanged != true) {
                        vm.title.value = it.title
                    }
                }
                is AppWebView.Event.ProgressChanged -> vm.progress.value = it.progress
                is AppWebView.Event.ShowFileChooser -> {
                    chooseFileEvent = it
                    startActivityForResultSafely(it.fileChooserParams.createIntent(), CHOOSE_FILE)
                }
            } }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CHOOSE_FILE -> {
                val event = chooseFileEvent
                chooseFileEvent = null
                if (event != null) {
                    val uris = WebChromeClient.FileChooserParams.parseResult(resultCode, data)
                    if (!uris.isNullOrEmpty())
                        event.filePathCallback.onReceiveValue(uris)
                }
            }

            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        if (webview.canGoBack())
            webview.goBack()
        else
            finish()
    }


}
