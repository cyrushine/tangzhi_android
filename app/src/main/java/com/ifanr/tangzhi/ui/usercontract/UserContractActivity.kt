package com.ifanr.tangzhi.ui.usercontract

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.observeToast
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.statusBar
import com.ifanr.tangzhi.ui.widgets.observeLoadingLiveData
import kotlinx.android.synthetic.main.activity_user_contract.*

/**
 * 用户协议
 */
@Route(path = Routes.userContract)
class UserContractActivity : BaseViewModelActivity() {

    companion object {
        private const val TAG = "UserContractActivity"
    }

    private lateinit var vm: UserContractViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_contract)
        statusBar(whiteText = false)

        toolbar.close.setOnClickListener { finish() }
        webview.bind(this)

        vm = viewModel()
        observeToast(vm.toast)
        observeLoadingLiveData(vm.loading)
        vm.contract.observe(this, Observer { it?.also {
            toolbar.titleTv.text = it.title
            webview.loadDataWithBaseURL(
                null, it.content, "text/html", "UTF-8", null)
        } })
    }
}
