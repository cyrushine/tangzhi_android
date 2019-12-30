package com.ifanr.tangzhi.ui.product.comments.review

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ui.base.dialog.BaseDialog
import com.ifanr.tangzhi.ui.base.dialog.BaseDialogFragment
import com.ifanr.tangzhi.ui.base.dialog.BaseViewModelDialogFragment
import com.ifanr.tangzhi.ui.base.dialog.viewModelOf
import com.ifanr.tangzhi.ui.widgets.ProductTag
import kotlinx.android.synthetic.main.product_tag_dialog.*

class ProductTagDialogFragment: BaseViewModelDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val vm: ReviewViewModel = viewModelOf(requireActivity())
        return ProductTagDialog(vm.tags.value?.map { ProductTag(it) } ?: emptyList(),
            requireContext())
    }
}

class ProductTagDialog(
    private val data: List<ProductTag>,
    context: Context
) : BaseDialog(context) {

    init {
        setCanceledOnTouchOutside(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_tag_dialog)
        close.setOnClickListener { dismiss() }
        list.setData(data)
    }
}