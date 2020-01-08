package com.ifanr.tangzhi.ui.comment.widget.model

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.TextAppearanceSpan
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ui.base.epoxy.KotlinEpoxyHolder
import com.ifanr.tangzhi.ext.avatar
import com.ifanr.tangzhi.model.Comment
import com.ifanr.tangzhi.ui.widgets.DateTextView

@EpoxyModelClass(layout = R.layout.comment_child_fold)
abstract class ChildFoldModel: EpoxyModelWithHolder<ChildFoldModel.Holder>() {

    @EpoxyAttribute
    lateinit var data: Comment

    override fun bind(holder: Holder) {
        Glide.with(holder.avatar).avatar().load(data.createdByAvatar).into(holder.avatar)
        holder.name.text = data.createdByName
        holder.date.setDatetime(data.createdAt)
        holder.content.text = SpannableStringBuilder()
            .append("@${data.replayToName}，",
                TextAppearanceSpan(holder.ctx, R.style.CommentChildReplay),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            .append(data.content)
    }

    class Holder: KotlinEpoxyHolder() {
        val avatar by bind<ImageView>(R.id.avatar)
        val name by bind<TextView>(R.id.name)
        val date by bind<DateTextView>(R.id.date)
        val content by bind<TextView>(R.id.content)
    }
}