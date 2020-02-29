package com.ifanr.tangzhi.ui.widgets

import android.content.Context
import android.net.Uri
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.SoundEffectConstants
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.Const
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getColorCompat
import com.ifanr.tangzhi.route.Routes

/**
 * 通用的行内文本链接
 */
open class TextLinkSpan (
    private val uri: Uri,
    ctx: Context
): ClickableSpan() {

    private val linkColor = ctx.getColorCompat(R.color.base_link)

    override fun updateDrawState(ds: TextPaint) {
        ds.color = linkColor
    }

    override fun onClick(widget: View) {
        ARouter.getInstance().build(Routes.browser)
            .withString(Routes.browserUrl, uri.toString())
            .navigation(widget.context)
        widget.playSoundEffect(SoundEffectConstants.CLICK)
    }

}

class UserAgreementSpan (
    ctx: Context
): TextLinkSpan(uri = Uri.parse(""), ctx = ctx) {

    override fun onClick(widget: View) {
        ARouter.getInstance().build(Routes.userContract)
            .navigation(widget.context)
        widget.playSoundEffect(SoundEffectConstants.CLICK)
    }

}

class PrivacyPolicySpan(ctx: Context): TextLinkSpan(uri = Uri.parse(Const.privacyPolicyUri), ctx = ctx)