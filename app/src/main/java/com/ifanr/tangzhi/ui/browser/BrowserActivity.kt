package com.ifanr.tangzhi.ui.browser

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.Routes
import com.ifanr.tangzhi.ext.appLabelString
import com.ifanr.tangzhi.ext.viewModelOf
import com.ifanr.tangzhi.ui.base.BaseActivity
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.statusBar
import com.ifanr.tangzhi.ui.widgets.AppWebView
import com.uber.autodispose.android.lifecycle.autoDispose
import kotlinx.android.synthetic.main.activity_browser.*
import kotlinx.android.synthetic.main.activity_product_param.toolbar
import javax.inject.Inject

@Route(path = Routes.browser)
class BrowserActivity : BaseViewModelActivity() {

    @Autowired(name = Routes.browserUrl)
    @JvmField
    var url = ""

    @Autowired(name = Routes.browserTitle)
    @JvmField
    var title = ""

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
        webview.loadUrl(url)
    }

    private fun initWebview(vm: BrowserViewModel) {
        webview.bind(this)
        webview.events.autoDispose(this)
            .subscribe { when (it) {
                is AppWebView.Event.WebViewCrash -> finish()
                is AppWebView.Event.UrlChanged -> vm.url.value = it.url
                is AppWebView.Event.TitleChanged -> vm.title.value = it.title
                is AppWebView.Event.ProgressChanged -> vm.progress.value = it.progress
            } }
    }

    override fun onBackPressed() {
        if (webview.canGoBack())
            webview.goBack()
        else
            finish()
    }
}
