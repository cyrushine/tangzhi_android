package com.ifanr.tangzhi.ui.points.handbook

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import com.ifanr.tangzhi.ui.base.dialog.BaseAppDialog
import com.ifanr.tangzhi.ui.base.dialog.BaseDialogFragment

class PointHandBookDialogFragment: BaseDialogFragment() {
}

class PointHandBookDialog: BaseAppDialog {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, theme: Int) : super(context, theme)
    constructor(
        context: Context?,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}