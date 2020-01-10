package com.ifanr.tangzhi.ext

import android.app.Activity
import com.ifanr.tangzhi.R
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.filter.Filter

fun Activity.matisse(
    requestCode: Int,
    maxSelectable: Int = 1,
    filter: Filter? = null
) {
    Matisse.from(this).choose(setOf(MimeType.JPEG, MimeType.PNG)).apply {
        autoHideToolbarOnSingleTap(true)
        capture(false)
        countable(true)
        imageEngine(GlideEngine())
        maxSelectable(maxSelectable)
        showPreview(false)
        theme(R.style.Matisse_TangZhi)
        if (filter != null)
            addFilter(filter)
        forResult(requestCode)
    }
}