package com.ifanr.android.tangzhi.ui.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.ifanr.android.tangzhi.R
import com.ifanr.android.tangzhi.Routes
import com.ifanr.android.tangzhi.di.DaggerAppComponent
import com.ifanr.android.tangzhi.ext.*
import com.ifanr.android.tangzhi.model.Product
import com.ifanr.android.tangzhi.repository.product.ProductRepository
import com.ifanr.android.tangzhi.ui.base.BaseActivity
import com.ifanr.android.tangzhi.ui.product.indexes.IndexesDialogFragment
import com.ifanr.android.tangzhi.ui.product.posts.PostController
import com.ifanr.android.tangzhi.ui.statusBar
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

        val viewModel: ProductViewModel = viewModelOf(viewModelFactory)
        viewModel.product.observe(this, Observer {
            it?.also { invalidate(it) }
        })
        viewModel.errorOnLoad.observe(this, Observer {
            Log.e(TAG, it.message, it)
            toast(R.string.error_load_product)
        })
        viewModel.relatedProducts.observe(this, Observer {
            it?.also { relatedProductList.setData(it) }
        })
        viewModel.productList.observe(this, Observer {
            productList.setProductLists(it)
        })
        viewModel.load(productId)
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

        posts.setPosts(product.cachedPost, product.postCount)

        indexes.setOnClickListener {
            IndexesDialogFragment.show(product, supportFragmentManager)
        }
    }
}
