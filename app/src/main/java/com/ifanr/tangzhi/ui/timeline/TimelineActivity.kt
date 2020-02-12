package com.ifanr.tangzhi.ui.timeline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.ioTask
import com.ifanr.tangzhi.ext.toast
import com.ifanr.tangzhi.route.Extra
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.statusBar
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_timeline.*

/**
 * 我的动态
 */
@Route(path = Routes.timeline, extras = Extra.signIn)
class TimelineActivity : BaseViewModelActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)
        statusBar(whiteText = false)

        toolBar.close.setOnClickListener { finish() }

        val vm: TimelineViewModel = viewModel()
        vm.loadList().ioTask(this).subscribe({
            list.submitPagedList(it)
        }, {
            toast(it.message ?: "load list fail")
        })
    }
}
