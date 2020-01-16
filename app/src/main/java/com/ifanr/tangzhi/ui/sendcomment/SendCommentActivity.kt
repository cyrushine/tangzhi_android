package com.ifanr.tangzhi.ui.sendcomment

import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ext.bind
import com.ifanr.tangzhi.ext.toast
import com.ifanr.tangzhi.route.Extra
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.statusBar
import com.ifanr.tangzhi.ui.widgets.SimpleToolBar
import com.ifanr.tangzhi.ui.widgets.observeLoadingLiveData
import kotlinx.android.synthetic.main.activity_send_comment.*

/**
 * 发表评论（回复点评）
 */
@Route(path = Routes.sendComment, extras = Extra.signIn)
class SendCommentActivity : BaseViewModelActivity() {

    @Autowired(name = Routes.sendCommentProductId)
    @JvmField
    var productId: String? = null

    @Autowired(name = Routes.sendCommentProductName)
    @JvmField
    var productName: String? = null

    @Autowired(name = Routes.sendCommentRootId)
    @JvmField
    var rootId: String? = null

    @Autowired(name = Routes.sendCommentParentId, required = false)
    @JvmField
    var parentId: String? = null

    @Autowired(name = Routes.sendCommentReplyId, required = false)
    @JvmField
    var replyId: String? = null

    @Autowired(name = Routes.sendCommentReplyTo, required = false)
    @JvmField
    var replyTo: Long? = 0L

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
            override fun onSend() { vm.sendComment() }
        }
        observeLoadingLiveData(vm.loading)
        vm.close.observe(this, Observer {
            if (it == true)
                finish()
        })
        vm.toast.observe(this, Observer { it?.also { toast(it) } })
        vm.productName.value = productName
        vm.init(productId ?: "", rootId ?: "", parentId, replyId, replyTo)
    }
}
