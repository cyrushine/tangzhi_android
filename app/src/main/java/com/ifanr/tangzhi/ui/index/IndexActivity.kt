package com.ifanr.tangzhi.ui.index

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.statusBar
import kotlinx.android.synthetic.main.activity_index.*

/**
 * 首页
 */
@Route(path = Routes.index)
class IndexActivity : BaseViewModelActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)
        statusBar(whiteText = false)
        vp.init(supportFragmentManager)
        bottomMenu.bind(vp)
    }
}
