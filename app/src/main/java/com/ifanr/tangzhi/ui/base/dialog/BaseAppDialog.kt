package com.ifanr.tangzhi.ui.base.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.ifanr.tangzhi.R

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
        val content = findViewById<ViewGroup>(R.id.content) as? ConstraintLayout
        if (content != null) {
            val child = layoutInflater.inflate(layoutRes, content, false)
            content.addView(child, ConstraintLayout.LayoutParams(0, 0).apply {
                leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
                rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            })
        }
    }
}