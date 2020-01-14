package com.ifanr.tangzhi.ui.base.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ui.base.widget.AppScrollView

/**
 * 糖纸风格的 dialog
 */
abstract class BaseAppDialog: BaseDialog {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, theme: Int) : super(context, theme)
    constructor(
        context: Context?,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener)

    init {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_app_dialog)
        findViewById<View>(R.id.close)?.setOnClickListener { dismiss() }
    }

    protected fun setContent(@LayoutRes layoutRes: Int) {
        val content = findViewById<ViewGroup>(R.id.content) as? AppScrollView
        if (content != null) {
            val child = layoutInflater.inflate(layoutRes, content, false)
            content.addView(child, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        }
    }
}