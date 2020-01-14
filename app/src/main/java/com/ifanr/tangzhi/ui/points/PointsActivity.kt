package com.ifanr.tangzhi.ui.points

import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.delay
import com.ifanr.tangzhi.route.Extra
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.points.handbook.PointHandBookDialogFragment
import com.ifanr.tangzhi.ui.statusBar
import kotlinx.android.synthetic.main.activity_points.*

/**
 * 我的积分
 */
@Route(path = Routes.points, extras = Extra.signIn)
class PointsActivity : BaseViewModelActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_points)
        statusBar(whiteText = false)

        val vm: PointsViewModel = viewModel()
        vm.records.observe(this, Observer { it?.also {
            list.submit(it)
        }})
        vm.userPoints.observe(this, Observer {
            card.setData(it ?: 0)
        })
        filter.onTypeChanged = { vm.type.value = it }

        card.onTitleClick = {
            PointHandBookDialogFragment().show(supportFragmentManager, null)
        }
        toolBar.close.setOnClickListener { finish() }
    }
}
