package com.ifanr.tangzhi.ui.product.comments.review

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ui.base.BaseDialog
import com.ifanr.tangzhi.ui.base.BaseDialogFragment
import com.ifanr.tangzhi.ui.widgets.ProductTag
import kotlinx.android.synthetic.main.product_tag_dialog.*

class ProductTagDialogFragment: BaseDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return ProductTagDialog(listOf(
            ProductTag(content = "更强的性能", count = 4, bgColor = Color.RED),
            ProductTag(content = "摄像头参数太好", count = 4, bgColor = Color.YELLOW),
            ProductTag(content = "人工福利好额呵呵", count = 4, bgColor = Color.GRAY),
            ProductTag(content = "和谐破碎机超级即机器", count = 4, bgColor = Color.BLUE),
            ProductTag(content = "更强的性能", count = 4, bgColor = Color.RED),
            ProductTag(content = "摄像头参数太好", count = 4, bgColor = Color.YELLOW),
            ProductTag(content = "人工福利好额呵呵", count = 4, bgColor = Color.GRAY),
            ProductTag(content = "和谐破碎机超级即机器", count = 4, bgColor = Color.BLUE),
            ProductTag(content = "更强的性能", count = 4, bgColor = Color.RED),
            ProductTag(content = "摄像头参数太好", count = 4, bgColor = Color.YELLOW),
            ProductTag(content = "人工福利好额呵呵", count = 4, bgColor = Color.GRAY),
            ProductTag(content = "和谐破碎机超级即机器", count = 4, bgColor = Color.BLUE),
            ProductTag(content = "更强的性能", count = 4, bgColor = Color.RED),
            ProductTag(content = "摄像头参数太好", count = 4, bgColor = Color.YELLOW),
            ProductTag(content = "人工福利好额呵呵", count = 4, bgColor = Color.GRAY),
            ProductTag(content = "和谐破碎机超级即机器", count = 4, bgColor = Color.BLUE),
            ProductTag(content = "更强的性能", count = 4, bgColor = Color.RED),
            ProductTag(content = "摄像头参数太好", count = 4, bgColor = Color.YELLOW),
            ProductTag(content = "人工福利好额呵呵", count = 4, bgColor = Color.GRAY),
            ProductTag(content = "和谐破碎机超级即机器", count = 4, bgColor = Color.BLUE),
            ProductTag(content = "更强的性能", count = 4, bgColor = Color.RED),
            ProductTag(content = "摄像头参数太好", count = 4, bgColor = Color.YELLOW),
            ProductTag(content = "人工福利好额呵呵", count = 4, bgColor = Color.GRAY),
            ProductTag(content = "和谐破碎机超级即机器", count = 4, bgColor = Color.BLUE),
            ProductTag(content = "更强的性能", count = 4, bgColor = Color.RED),
            ProductTag(content = "摄像头参数太好", count = 4, bgColor = Color.YELLOW),
            ProductTag(content = "人工福利好额呵呵", count = 4, bgColor = Color.GRAY),
            ProductTag(content = "和谐破碎机超级即机器", count = 4, bgColor = Color.BLUE),
            ProductTag(content = "更强的性能", count = 4, bgColor = Color.RED),
            ProductTag(content = "摄像头参数太好", count = 4, bgColor = Color.YELLOW),
            ProductTag(content = "人工福利好额呵呵", count = 4, bgColor = Color.GRAY),
            ProductTag(content = "和谐破碎机超级即机器", count = 4, bgColor = Color.BLUE),
            ProductTag(content = "更强的性能", count = 4, bgColor = Color.RED),
            ProductTag(content = "摄像头参数太好", count = 4, bgColor = Color.YELLOW),
            ProductTag(content = "人工福利好额呵呵", count = 4, bgColor = Color.GRAY),
            ProductTag(content = "和谐破碎机超级即机器", count = 4, bgColor = Color.BLUE),
            ProductTag(content = "更强的性能", count = 4, bgColor = Color.RED),
            ProductTag(content = "摄像头参数太好", count = 4, bgColor = Color.YELLOW),
            ProductTag(content = "人工福利好额呵呵", count = 4, bgColor = Color.GRAY),
            ProductTag(content = "和谐破碎机超级即机器", count = 4, bgColor = Color.BLUE),
            ProductTag(content = "更强的性能", count = 4, bgColor = Color.RED),
            ProductTag(content = "摄像头参数太好", count = 4, bgColor = Color.YELLOW),
            ProductTag(content = "人工福利好额呵呵", count = 4, bgColor = Color.GRAY),
            ProductTag(content = "和谐破碎机超级即机器", count = 4, bgColor = Color.BLUE),
            ProductTag(content = "更强的性能", count = 4, bgColor = Color.RED),
            ProductTag(content = "摄像头参数太好", count = 4, bgColor = Color.YELLOW),
            ProductTag(content = "人工福利好额呵呵", count = 4, bgColor = Color.GRAY),
            ProductTag(content = "和谐破碎机超级即机器", count = 4, bgColor = Color.BLUE),
            ProductTag(content = "更强的性能", count = 4, bgColor = Color.RED),
            ProductTag(content = "摄像头参数太好", count = 4, bgColor = Color.YELLOW),
            ProductTag(content = "人工福利好额呵呵", count = 4, bgColor = Color.GRAY),
            ProductTag(content = "和谐破碎机超级即机器", count = 4, bgColor = Color.BLUE),
            ProductTag(content = "更强的性能", count = 4, bgColor = Color.RED),
            ProductTag(content = "摄像头参数太好", count = 4, bgColor = Color.YELLOW),
            ProductTag(content = "人工福利好额呵呵", count = 4, bgColor = Color.GRAY),
            ProductTag(content = "和谐破碎机超级即机器", count = 4, bgColor = Color.BLUE),
            ProductTag(content = "更强的性能", count = 4, bgColor = Color.RED),
            ProductTag(content = "摄像头参数太好", count = 4, bgColor = Color.YELLOW),
            ProductTag(content = "人工福利好额呵呵", count = 4, bgColor = Color.GRAY),
            ProductTag(content = "和谐破碎机超级即机器", count = 4, bgColor = Color.BLUE),
            ProductTag(content = "更强的性能", count = 4, bgColor = Color.RED),
            ProductTag(content = "摄像头参数太好", count = 4, bgColor = Color.YELLOW),
            ProductTag(content = "人工福利好额呵呵", count = 4, bgColor = Color.GRAY),
            ProductTag(content = "和谐破碎机超级即机器", count = 4, bgColor = Color.BLUE),
            ProductTag(content = "更强的性能", count = 4, bgColor = Color.RED),
            ProductTag(content = "摄像头参数太好", count = 4, bgColor = Color.YELLOW),
            ProductTag(content = "人工福利好额呵呵", count = 4, bgColor = Color.GRAY),
            ProductTag(content = "和谐破碎机超级即机器", count = 4, bgColor = Color.BLUE),
            ProductTag(content = "更强的性能", count = 4, bgColor = Color.RED),
            ProductTag(content = "摄像头参数太好", count = 4, bgColor = Color.YELLOW),
            ProductTag(content = "人工福利好额呵呵", count = 4, bgColor = Color.GRAY),
            ProductTag(content = "和谐破碎机超级即机器", count = 4, bgColor = Color.BLUE)

        ),
            requireContext())
    }
}

class ProductTagDialog(
    private val data: List<ProductTag>,
    context: Context
) : BaseDialog(context) {

    init {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_tag_dialog)

        close.setOnClickListener { dismiss() }
        list.setData(data)
    }
}