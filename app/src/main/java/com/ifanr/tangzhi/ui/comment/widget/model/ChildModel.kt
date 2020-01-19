package com.ifanr.tangzhi.ui.comment.widget.model

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.TextAppearanceSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ui.base.epoxy.KotlinEpoxyHolder
import com.ifanr.tangzhi.ext.avatar
import com.ifanr.tangzhi.model.Comment
import com.ifanr.tangzhi.ui.widgets.DateTextView
import com.ifanr.tangzhi.ui.widgets.ReplyView
import com.ifanr.tangzhi.ui.widgets.UpView

@EpoxyModelClass(layout = R.layout.comment_child)
abstract class ChildModel: EpoxyModelWithHolder<ChildModel.Holder>() {

    @EpoxyAttribute
    lateinit var data: Comment

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onReplyClick: View.OnClickListener

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onUpClick: View.OnClickListener

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onOptionClick: View.OnClickListener

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    @DrawableRes
    var background = 0

    override fun bind(holder: Holder) {
        Glide.with(holder.avatar).avatar().load(data.createdByAvatar).into(holder.avatar)
        holder.name.text = data.createdByName
        holder.date.setDatetime(data.createdAt)
        holder.content.text = SpannableStringBuilder()
            .append("@${data.replayToName}，",
                TextAppearanceSpan(holder.ctx, R.style.CommentChildReplay),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            .append(data.content)
        holder.reply.setCount(data.replyCount)
        holder.up.setCount(data.upvote)
        holder.up.isSelected = data.voted
        holder.reply.setOnClickListener(onReplyClick)
        holder.up.setOnClickListener(onUpClick)
        holder.option.setOnClickListener(onOptionClick)
        holder.view.setBackgroundResource(background)
    }

    class Holder: KotlinEpoxyHolder() {
        val avatar by bind<ImageView>(R.id.avatar)
        val name by bind<TextView>(R.id.name)
        val date by bind<DateTextView>(R.id.date)
        val content by bind<TextView>(R.id.content)
        val reply by bind<ReplyView>(R.id.reply)
        val up by bind<UpView>(R.id.up)
        val option by bind<View>(R.id.optionBtn)
    }
}