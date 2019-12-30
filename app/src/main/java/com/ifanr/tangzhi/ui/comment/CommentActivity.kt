package com.ifanr.tangzhi.ui.comment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.statusBar
import kotlinx.android.synthetic.main.activity_comment.*

/**
 * 评论列表
 */
@Route(path = Routes.comment)
class CommentActivity : BaseViewModelActivity() {

    @Autowired(name = Routes.commentProductId)
    @JvmField
    var productId = ""

    @Autowired(name = Routes.commentProductName)
    @JvmField
    var productName = ""

    @Autowired(name = Routes.commentReviewId)
    @JvmField
    var reviewId = ""

    lateinit var vm: CommentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        statusBar(whiteText = false)

        vm = viewModel()
        vm.productName.observe(this, Observer { toolbar.titleTv.text = it })
        vm.comments.observe(this, Observer { it?.also { list.setData(it) }})

        vm.productId.value = productId
        vm.productName.value = productName
        vm.reviewId.value = reviewId

        sendCommentBtn.setOnClickListener {
            ARouter.getInstance().build(Routes.sendComment)
                .withString(Routes.sendCommentProductId, productId)
                .withString(Routes.sendCommentProductName, productName)
                .withString(Routes.sendCommentParentId, reviewId)
                .navigation(this)
        }
        toolbar.close.setOnClickListener { finish() }
    }
}
