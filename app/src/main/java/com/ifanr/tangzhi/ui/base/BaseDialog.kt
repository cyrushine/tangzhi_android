package com.ifanr.tangzhi.ui.base

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import androidx.appcompat.app.AppCompatDialog
import com.ifanr.tangzhi.ext.dp2px
import kotlin.math.roundToInt

abstract class BaseDialog: AppCompatDialog {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, theme: Int) : super(context, theme)
    constructor(
        context: Context?,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener)

    override fun show() {
        super.show()

        window?.also { w ->
            w.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val dm = context.resources.displayMetrics
            val lp = w.attributes.apply {
                width = dm.widthPixels - context.dp2px(25) * 2
                height = dm.heightPixels.times(0.8).roundToInt()
                gravity = Gravity.CENTER
            }
            w.attributes = lp
        }
    }
}