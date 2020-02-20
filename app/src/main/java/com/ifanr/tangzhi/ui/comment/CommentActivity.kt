package com.ifanr.tangzhi.ui.comment

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.observeToast
import com.ifanr.tangzhi.model.Comment
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.comment.widget.CommentList
import com.ifanr.tangzhi.ui.statusBar
import com.ifanr.tangzhi.ui.widgets.observeLoadingLiveData
import kotlinx.android.synthetic.main.activity_comment.*

/**
 * 评论列表
 */
@Route(path = Routes.comment)
class CommentActivity : BaseViewModelActivity() {

    companion object {
        private const val TAG = "CommentActivity"
    }

    @Autowired(name = Routes.commentProductId)
    @JvmField
    var productId = ""

    @Autowired(name = Routes.commentProductName)
    @JvmField
    var productName = ""

    @Autowired(name = Routes.commentReviewId)
    @JvmField
    var reviewId = ""

    @JvmField
    @Autowired(name = Routes.commentReviewCreatedBy)
    var reviewCreatedBy = 0L

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
        observeLoadingLiveData(vm.loading)
        observeToast(vm.toast)

        // 底部的回复按钮（回复点评）
        sendCommentBtn.setOnClickListener {
            val request = ARouter.getInstance().build(Routes.sendComment)
                .withString(Routes.sendCommentProductId, productId)
                .withString(Routes.sendCommentProductName, productName)
                .withString(Routes.sendCommentRootId, reviewId)
            request.navigation(this)
        }

        toolbar.close.setOnClickListener { finish() }
        list.setListener(object: CommentList.Listener {

            /**
             * 回复评论
             */
            override fun onReplyClick(id: String) {
                vm.comments.value?.flatMap { it.children + it }?.find { it.id == id }?.also { comment ->
                    val postCard = ARouter.getInstance().build(Routes.sendComment)
                        .withString(Routes.sendCommentProductId, productId)
                        .withString(Routes.sendCommentProductName, productName)
                        .withString(Routes.sendCommentRootId, reviewId)

                    if (comment.type == Comment.TYPE_COMMENT) {
                        postCard.withString(Routes.sendCommentParentId,
                            if (comment.parentId.isEmpty()) comment.id else comment.parentId)
                            .withString(Routes.sendCommentReplyId, comment.id)
                            .withLong(Routes.sendCommentReplyTo, comment.createdById)
                    }
                    postCard.navigation(this@CommentActivity)
                }
            }

            /**
             * 点赞
             */
            override fun onVoteClick(id: String) {
                vm.onVoteClick(id)
            }

            /**
             * 评论菜单
             */
            override fun onOptionClick(id: String) {
                super.onOptionClick(id)
            }

            override fun onLoadMore() { vm.tryLoadNextPage() }
            override fun onChildLoadMoreClick(id: String) { vm.loadChild(id) }
        })
    }
}
