package com.ifanr.tangzhi.ui.widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.bumptech.glide.Glide
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.avatar
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.ext.inflateInto
import com.ifanr.tangzhi.ext.setPadding

/**
 * 头像列表
 */
class AvatarGroup: ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        inflateInto(R.layout.avatar_group)
        children.forEach { it.background = RoundedRectDrawable(Color.WHITE) }
    }

    fun setAvatars(avatars: List<String>) {
        children.map { it as ImageView }.forEachIndexed { index, child ->
            val avatar = avatars.getOrNull(index)
            child.visibility = if (avatar == null) View.GONE else View.VISIBLE
            if (avatar != null) {
                Glide.with(this).avatar().load(avatar).into(child)
            }
        }
    }

}