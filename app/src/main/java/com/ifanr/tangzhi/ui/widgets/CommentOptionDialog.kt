package com.ifanr.tangzhi.ui.widgets

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ui.base.dialog.BaseBottomSheetDialog
import kotlinx.android.synthetic.main.dialog_comment_option.*

/**
 * 点评/评论的操作面板
 */
class CommentOptionDialog: BaseBottomSheetDialog {

    companion object {
        fun show(ctx: Context, editMode: Boolean, l: Listener) {
            CommentOptionDialog(ctx).apply {
                this.editMode = editMode
                this.listener = l
            }.show()
        }
    }

    interface Listener {

        /**
         * 分享给好友
         */
        fun onShare() {}

        /**
         * 复制
         */
        fun onCopy() {}

        /**
         * 编辑
         */
        fun onUpdate() {}

        /**
         * 删除
         */
        fun onDelete() {}

        /**
         * 举报
         */
        fun onCatchUp() {}
    }

    private var listener: Listener = object : Listener {}

    /**
     * 如果是编辑模式，则会显示「编辑」和「删除」按钮
     */
    private var editMode = true

    constructor(context: Context) : super(context)
    constructor(context: Context, theme: Int) : super(context, theme)
    constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_comment_option)

        share.setOnClickListener {
            listener.onShare()
            dismiss()
        }

        copy.setOnClickListener {
            listener.onCopy()
            dismiss()
        }

        update.setOnClickListener {
            listener.onUpdate()
            dismiss()
        }

        delete.setOnClickListener {
            listener.onDelete()
            dismiss()
        }

        catchUp.setOnClickListener {
            listener.onCatchUp()
            dismiss()
        }

        cancel.setOnClickListener { dismiss() }

        listOf(update, delete).forEach {
            it.visibility = if (editMode) View.VISIBLE else View.GONE
        }
    }
}