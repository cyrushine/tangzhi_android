package com.ifanr.tangzhi.ui.postlist

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
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.statusBar
import kotlinx.android.synthetic.main.activity_post_list.*
import javax.inject.Inject

@Route(path = Routes.postList)
class PostListActivity : BaseViewModelActivity() {

    @Autowired(name = Routes.postListProductId)
    @JvmField
    var productId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_list)
        statusBar(whiteText = false)
        toolbar.close.setOnClickListener { finish() }

        val vm: PostListViewModel = viewModel()
        vm.list.observe(this, Observer {
            it?.also { list.setData(it) }
        })
        vm.load(productId)
    }
}
