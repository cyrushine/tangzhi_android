package com.ifanr.tangzhi.ui.product

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.Routes
import com.ifanr.tangzhi.ext.*
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.ui.base.BaseActivity
import com.ifanr.tangzhi.ui.product.indexes.IndexesDialogFragment
import com.ifanr.tangzhi.ui.statusBar
import kotlinx.android.synthetic.main.activity_product.*
import javax.inject.Inject

@Route(path = Routes.product)
class ProductActivity : BaseActivity() {

    companion object {
        private const val TAG = "ProductActivity"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @JvmField
    @Autowired(name = Routes.productId)
    var productId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        statusBar(whiteText = true)

        productParam.setOnClickListener {
            ARouter.getInstance().build(Routes.productParam)
                .withString(Routes.productParamId, productId)
                .navigation(this)
        }

        val vm: ProductViewModel = viewModelOf(viewModelFactory)
        vm.product.observe(this, Observer {
            it?.also { invalidate(it) }
        })
        vm.errorOnLoad.observe(this, Observer {
            Log.e(TAG, it.message, it)
            toast(R.string.error_load_product)
        })
        vm.relatedProducts.observe(this, Observer {
            it?.also { relatedProductList.setData(it) }
        })
        vm.productList.observe(this, Observer {
            productList.setProductLists(it)
        })
        vm.paramVisiable.observe(this, Observer {
            productParam.visibility = if (it == true) View.VISIBLE else View.GONE
        })
        vm.load(productId)
    }

    private fun invalidate(product: Product) {
        val themeColor = product.themeColor
        Glide.with(this).default().load(product.coverImage).into(coverIV)
        coverIV.setThemeColor(themeColor)
        root.setBackgroundColor(themeColor)
        following.setOnClickListener { following.toggleState() }
        nameTv.text = product.name

        indexes.setScore(product.rating)
        summaryTv.text = product.description

        posts.setData(product.id, product.cachedPost, product.postCount.toInt())

        indexes.setOnClickListener {
            IndexesDialogFragment.show(product, supportFragmentManager)
        }
    }
}
