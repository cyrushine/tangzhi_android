package com.ifanr.tangzhi.ui.productparam

import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.productparam.widgets.Controller
import com.ifanr.tangzhi.ui.statusBar
import kotlinx.android.synthetic.main.activity_product_param.*

@Route(path = Routes.productParam)
class ProductParamActivity : BaseViewModelActivity() {

    @Autowired(name = Routes.productParamId)
    @JvmField
    var productId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_param)
        statusBar(whiteText = true)

        toolbar.close.setOnClickListener { finish() }
        val controller = Controller()
        list.setController(controller)

        val vm: ProductParamViewModel = viewModel()
        vm.productName.observe(this, Observer {
            toolbar.titleTv.text = it
        })
        vm.params.observe(this, Observer {
            it?.also { controller.setData(it) }
        })
        vm.load(productId)
    }
}

