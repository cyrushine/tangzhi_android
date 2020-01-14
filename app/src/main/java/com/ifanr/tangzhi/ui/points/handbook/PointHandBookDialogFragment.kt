package com.ifanr.tangzhi.ui.points.handbook

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ui.base.dialog.BaseAppDialog
import com.ifanr.tangzhi.ui.base.dialog.BaseDialogFragment

/**
 * 积分说明 dialog
 */
class PointHandBookDialogFragment: BaseDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return PointHandBookDialog(context)
    }
}

class PointHandBookDialog: BaseAppDialog {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, theme: Int) : super(context, theme)
    constructor(
        context: Context?,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener)

    init {
        heightPercent = 0.8f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent(R.layout.dialog_point_hand_book)
    }
}