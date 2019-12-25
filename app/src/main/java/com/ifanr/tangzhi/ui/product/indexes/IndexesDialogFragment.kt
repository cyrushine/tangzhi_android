package com.ifanr.tangzhi.ui.product.indexes

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.ui.base.BaseDialog
import com.ifanr.tangzhi.ui.base.BaseDialogFragment
import org.parceler.Parcels

/**
 * 模范指数弹窗
 */
class IndexesDialogFragment: BaseDialogFragment() {

    companion object {

        private const val ARG_RATING = "ARG_RATING"

        fun show(product: Product, fm: FragmentManager, tag: String? = null): IndexesDialogFragment {
            return IndexesDialogFragment().apply {
                arguments = bundleOf(ARG_RATING to Parcels.wrap(ProductRating(product)))
                show(fm, tag)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return IndexesDialog(requireContext(), Parcels.unwrap(arguments?.getParcelable(ARG_RATING)))
    }
}

class IndexesDialog (
    ctx: Context,
    private val data: ProductRating
): BaseDialog(ctx) {

    init {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_product_indexes)

        val controller = Controller()
        findViewById<EpoxyRecyclerView>(R.id.list)?.apply {
            layoutManager = LinearLayoutManager(context)
            setController(controller)
        }
        controller.setData(data)

        findViewById<View>(R.id.close)?.setOnClickListener { dismiss() }
    }
}