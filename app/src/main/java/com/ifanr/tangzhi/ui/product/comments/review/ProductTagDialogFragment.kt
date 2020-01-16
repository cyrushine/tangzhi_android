package com.ifanr.tangzhi.ui.product.comments.review

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.Observer
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.model.Comment
import com.ifanr.tangzhi.ui.base.autoDispose
import com.ifanr.tangzhi.ui.base.dialog.*
import com.ifanr.tangzhi.ui.widgets.ProductTag
import com.ifanr.tangzhi.ui.widgets.ProductTagList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.product_tag_dialog.*

class ProductTagDialogFragment: BaseViewModelDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val vm: ReviewViewModel = viewModelOf(requireActivity())
        return ProductTagDialog(vm, requireContext())
    }
}

class ProductTagDialog(
    private val vm: ReviewViewModel,
    context: Context
) : BaseDialog(context) {

    init {
        setCanceledOnTouchOutside(false)
    }

    private val tagsObserver = Observer<List<Comment>> {
        list?.setData(it?.map { ProductTag(it) } ?: emptyList())
    }

    private var list: ProductTagList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_tag_dialog)
        close.setOnClickListener { dismiss() }

        list = findViewById(R.id.list)
        list?.onTagClick = { vm.onTagClick(it, showLoading = false) }
        input.onTextSend = {
            vm.addProductTag(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { input.isEnabled = false }
                .doOnTerminate { input.isEnabled = true }
                .autoDispose(this)
                .subscribe {
                    input.clearText()
                }
        }
        vm.tags.observeForever(tagsObserver)
    }

    override fun onStop() {
        super.onStop()
        vm.tags.removeObserver(tagsObserver)
    }
}