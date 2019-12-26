package com.ifanr.tangzhi.ui.product.comments.review

import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ui.base.BaseFragment
import com.ifanr.tangzhi.ui.base.BaseViewModelFragment
import com.ifanr.tangzhi.ui.base.viewModelOf
import com.ifanr.tangzhi.ui.product.ProductViewModel
import com.ifanr.tangzhi.ui.product.indexes.IndexesDialogFragment
import com.ifanr.tangzhi.ui.widgets.ProductTag
import kotlinx.android.synthetic.main.review_fragment.*

/**
 * 点评
 */
class ReviewFragment : BaseViewModelFragment() {

    companion object {
        private const val TAG = "ReviewFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.review_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val productViewModel =
            ViewModelProviders.of(requireActivity())[ProductViewModel::class.java]
        val vm: ReviewViewModel = viewModelOf()

        productViewModel.product.observe(this, Observer {
            it?.also { product ->
                reviewIndexesCard.set(product.rating, product.reviewCount.toInt(), product.orgRating.size)
                reviewIndexesCard.setOnClickListener {
                    IndexesDialogFragment.show(product, childFragmentManager)
                }
            }
        })

        productViewModel.product.observe(this, Observer { vm.product.value = it })

        vm.tags.observe(this, Observer {
            it?.map { ProductTag(content = it.content, count = it.upvote, bgColor = it.theme) }
                ?.also {
                    tagCard.setData(it)
                    tagCard.openTagDialog = {
                        ProductTagDialogFragment().show(childFragmentManager, null)
                    }
                }
        })
    }

}
