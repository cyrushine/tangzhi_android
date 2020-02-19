package com.ifanr.tangzhi.ui.product

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ext.*
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.product.comments.review.ReviewFragment
import com.ifanr.tangzhi.ui.product.comments.review.ReviewViewModel
import com.ifanr.tangzhi.ui.product.indexes.IndexesDialogFragment
import com.ifanr.tangzhi.ui.product.widgets.FollowingView
import com.ifanr.tangzhi.ui.product.widgets.ProductContainer
import com.ifanr.tangzhi.ui.share.ShareProductReq
import com.ifanr.tangzhi.ui.statusBar
import com.ifanr.tangzhi.ui.widgets.dismissLoading
import com.ifanr.tangzhi.ui.widgets.showLoading
import kotlinx.android.synthetic.main.activity_product.*

@Route(path = Routes.product)
class ProductActivity : BaseViewModelActivity() {

    companion object {
        private const val TAG = "ProductActivity"
    }

    @JvmField
    @Autowired(name = Routes.productId)
    var productId: String = ""

    lateinit var vm: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        statusBar(whiteText = true)

        vm = viewModel()
        productParam.setOnClickListener {
            ARouter.getInstance().build(Routes.productParam)
                .withString(Routes.productParamId, productId)
                .navigation(this)
        }

        approve.setOnClickListener { openCreateReviewPage(score = 10.0f) }
        oppose.setOnClickListener { openCreateReviewPage(score = 4f) }

        vm.product.observe(this, Observer {
            it?.also { invalidate(it) }
        })

        vm.errorOnLoad.observe(this, Observer {
            Log.e(TAG, it.message, it)
            toast(R.string.error_load_product)
        })

        vm.relatedProducts.observe(this, Observer {
            it?.also { relatedProductList.setData(it) }
        })

        vm.productList.observe(this, Observer {
            productList.setProductLists(productId, it)
        })

        vm.paramVisiable.observe(this, Observer {
            productParam.visibility = if (it == true) View.VISIBLE else View.GONE
        })

        vm.isFollowed.observe(this, Observer {
            following.state = if (it == true)
                FollowingView.State.FOLLOWED
            else
                FollowingView.State.UN_FOLLOW
        })

        vm.loading.observe(this, Observer { it?.also {
            when {
                it == 0 -> showLoading()
                it > 0 -> showLoading(delay = true)
                it < 0 -> dismissLoading()
            }
        }})

        vm.toast.observe(this, Observer { it?.also { toast(it) }})

        vm.review.observe(this, Observer { it?.also {
            val up = it.rating >= 6f
            approve.isChecked = up
            oppose.isChecked = !up
        }})

        vm.load(productId)

        following.setOnClickListener { vm.onFollowClick() }

        commentVP.adapter = object: FragmentStatePagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {
                return ReviewFragment()
            }

            override fun getCount(): Int {
                return 1
            }
        }

        toolBar.close.setOnClickListener { onCloseClick() }

        val reviewViewModel: ReviewViewModel = viewModel()
        reviewViewModel.reviewCount.observe(this, Observer {
            reviewIndicatorCount.text = it?.toString() ?: "0"
        })
    }

    /**
     * 返回事件
     */
    private fun onCloseClick() {
        if (root.state == ProductContainer.State.EXPAND) {
            root.fold()
            return
        }
        finish()
    }

    override fun onBackPressed() {
        onCloseClick()
    }

    private fun invalidate(product: Product) {
        val themeColor = product.themeColor
        banner.setThemeColor(themeColor)
        banner.setImages(product.allImages)
        root.setBackgroundColor(themeColor)
        nameTv.text = product.name

        indexes.setScore(product.rating)

        posts.setData(product.id, product.cachedPost, product.postCount.toInt())

        indexes.setOnClickListener {
            IndexesDialogFragment.show(product, supportFragmentManager)
        }
        summaryTv.text = product.description

        root.addBottomPanelDraggedListener {
            commentIndicator.setRotatePercent(it)
        }
        shareButton.setOnClickListener {
            ARouter.getInstance().build(Routes.share)
                .withParcelable(Routes.shareObject, ShareProductReq(
                    id = product.id, coverImage = product.coverImage, title = product.name))
                .navigation(this)
        }
    }

    private fun openCreateReviewPage(score: Float = 0f) {
        val productId = vm.product.value?.id
        val productName = vm.product.value?.name
        if (!productId.isNullOrEmpty()){
            ARouter.getInstance().build(Routes.sendReview)
                .withString(Routes.sendReviewProductId, productId)
                .withString(Routes.sendReviewProductName, productName)
                .withFloat(Routes.sendReviewScore, score)
                .navigation(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            ReviewFragment.SEND_REVIEW -> {
                val vm: ReviewViewModel = viewModel()
                vm.refreshToLatest.value = true
            }

            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
