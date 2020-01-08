package com.ifanr.tangzhi.ui.relatedproducts

import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.statusBar
import kotlinx.android.synthetic.main.activity_related_products.*

@Route(path = Routes.relatedProducts)
class RelatedProductsActivity : BaseViewModelActivity() {

    @Autowired(name = Routes.relatedProductId)
    @JvmField
    var productId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_related_products)
        statusBar(whiteText = false)
        toolbar.close.setOnClickListener { finish() }

        val vm: RelatedProductsViewModel = viewModel()
        vm.products.observe(this, Observer {
            it?.also { list.setData(it) }
        })
        vm.load(productId)
    }
}
