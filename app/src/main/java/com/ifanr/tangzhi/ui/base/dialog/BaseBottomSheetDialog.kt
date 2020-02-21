package com.ifanr.tangzhi.ui.base.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ifanr.tangzhi.ext.dp2px
import kotlin.math.roundToInt

/**
 * 宽度是屏幕宽度
 * 底部在屏幕底下
 */
abstract class BaseBottomSheetDialog: BottomSheetDialog {

    constructor(context: Context) : super(context)
    constructor(context: Context, theme: Int) : super(context, theme)
    constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener)

    override fun show() {
        super.show()

        window?.also { w ->
            w.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val dm = context.resources.displayMetrics
            val lp = w.attributes.apply {
                width = dm.widthPixels
                height = dm.heightPixels
                gravity = Gravity.BOTTOM
            }
            w.attributes = lp
        }
    }
}