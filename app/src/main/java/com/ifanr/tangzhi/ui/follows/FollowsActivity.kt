package com.ifanr.tangzhi.ui.follows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.ioTask
import com.ifanr.tangzhi.ext.observeToast
import com.ifanr.tangzhi.ext.toast
import com.ifanr.tangzhi.route.Extra
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.statusBar
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_follows.*

/**
 * 我的关注
 */
@Route(path = Routes.follows, extras = Extra.signIn)
class FollowsActivity : BaseViewModelActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follows)
        statusBar(whiteText = false)

        toolBar.close.setOnClickListener { finish() }

        val vm: FollowsViewModel = viewModel()
        observeToast(vm.toast)
        vm.list.observe(this, Observer { it?.also {
            list.submitList(it)
        } })
    }
}
