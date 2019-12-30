package com.ifanr.tangzhi.ui.sendcomment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.Routes
import com.ifanr.tangzhi.ext.bind
import com.ifanr.tangzhi.ext.viewModelOf
import com.ifanr.tangzhi.ui.base.BaseActivity
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.statusBar
import com.ifanr.tangzhi.ui.widgets.SimpleToolBar
import kotlinx.android.synthetic.main.activity_send_comment.*

/**
 * 发表评论（回复点评）
 */
@Route(path = Routes.sendComment)
class SendCommentActivity : BaseViewModelActivity() {

    @Autowired(name = Routes.sendCommentParentId)
    @JvmField
    var productId = ""

    @Autowired(name = Routes.sendCommentProductName)
    @JvmField
    var productName = ""

    @Autowired(name = Routes.sendCommentParentId)
    @JvmField
    var parentId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_comment)
        statusBar(whiteText = false)

        val vm: SendCommentViewModel = viewModel()
        vm.productName.observe(this, Observer { toolBar.setTitle(it ?: "") })
        contentEt.bind(this, vm.content)
        vm.sendButtonEnable.observe(this, Observer {
            toolBar.sendButtonEnable = it == true
        })
        toolBar.listener = object: SimpleToolBar.Listener {
            override fun onCancel() { finish() }
            override fun onSend() { finish() }
        }
        vm.productId.value = productId
        vm.productName.value = productName
        vm.parentId.value = parentId
    }
}
