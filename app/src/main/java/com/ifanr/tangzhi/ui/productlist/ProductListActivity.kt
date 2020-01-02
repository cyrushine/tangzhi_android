package com.ifanr.tangzhi.ui.productlist

import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.statusBar
import kotlinx.android.synthetic.main.activity_product_list.*

@Route(path = Routes.productList)
class ProductListActivity : BaseViewModelActivity() {

    @JvmField
    @Autowired(name = Routes.productListProductId)
    var productId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        statusBar(whiteText = false)

        toolbar.close.setOnClickListener { finish() }

        val vm: ProductLIstViewModel = viewModel()
        vm.list.observe(this, Observer {
            it?.also { list.submitList(it) }
        })
        vm.load(productId)
    }
}
