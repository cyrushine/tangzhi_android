package com.ifanr.android.tangzhi.ui.productparam

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.ifanr.android.tangzhi.R
import com.ifanr.android.tangzhi.Routes
import com.ifanr.android.tangzhi.ext.viewModelOf
import com.ifanr.android.tangzhi.ui.base.BaseActivity
import com.ifanr.android.tangzhi.ui.productparam.widgets.Controller
import com.ifanr.android.tangzhi.ui.statusBar
import kotlinx.android.synthetic.main.activity_product_param.*
import javax.inject.Inject

@Route(path = Routes.productParam)
class ProductParamActivity : BaseActivity() {

    @Autowired(name = Routes.productParamId)
    @JvmField
    var productId = ""

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_param)
        statusBar(whiteText = true)

        toolbar.close.setOnClickListener { finish() }
        val controller = Controller()
        list.setController(controller)

        val vm: ProductParamViewModel = viewModelOf(factory)
        vm.productName.observe(this, Observer {
            toolbar.titleTv.text = it
        })
        vm.params.observe(this, Observer {
            it?.also { controller.params = it }
        })
        vm.load(productId)
    }
}

