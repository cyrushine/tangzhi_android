package com.ifanr.tangzhi.ui.relatedproducts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.Routes
import com.ifanr.tangzhi.ext.viewModelOf
import com.ifanr.tangzhi.ui.base.BaseActivity
import com.ifanr.tangzhi.ui.statusBar
import kotlinx.android.synthetic.main.activity_related_products.*
import javax.inject.Inject

@Route(path = Routes.relatedProducts)
class RelatedProductsActivity : BaseActivity() {

    @Autowired(name = Routes.relatedProductId)
    @JvmField
    var productId: String = ""

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_related_products)
        statusBar(whiteText = false)
        toolbar.close.setOnClickListener { finish() }

        val vm: RelatedProductsViewModel = viewModelOf(factory)
        vm.products.observe(this, Observer {
            it?.also { list.setData(it) }
        })
        vm.load(productId)
    }
}
