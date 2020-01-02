package com.ifanr.tangzhi.ui.product.comments.review

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.epoxy.KotlinEpoxyHolder
import com.ifanr.tangzhi.ext.avatar
import com.ifanr.tangzhi.model.Comment
import com.ifanr.tangzhi.ui.product.comments.review.widget.ImageTable
import com.ifanr.tangzhi.ui.widgets.ReplyView
import com.ifanr.tangzhi.ui.widgets.UpView
import com.ifanr.tangzhi.ui.widgets.DateTextView
import com.ifanr.tangzhi.ui.widgets.EditorChoiceImageView
import com.ifanr.tangzhi.ui.widgets.RatingBar

@EpoxyModelClass(layout = R.layout.product_comment)
abstract class ProductReviewModel: EpoxyModelWithHolder<ProductReviewModel.Holder>() {

    companion object {
        private const val TAG = "ProductReviewModel"
    }

    @EpoxyAttribute
    lateinit var comment: Comment

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onReplyClick: View.OnClickListener

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onClick: View.OnClickListener

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var dateVisiable = false

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var optionVisiable = false

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var contentExpanded = false

    override fun bind(holder: Holder) {
        Glide.with(holder.avatar).avatar().load(comment.createdByAvatar).into(holder.avatar)
        holder.name.text = comment.createdByName
        holder.content.text = comment.content
        holder.content.visibility = if (comment.content.isNotEmpty()) View.VISIBLE else View.GONE
        holder.editorChoice.visibility = if (comment.recommended) View.VISIBLE else View.GONE
        holder.ratingBar.setProgress(comment.rating)
        holder.imageTable.setData(comment.images)
        holder.imageTable.visibility = if (comment.images.isNotEmpty()) View.VISIBLE else View.GONE
        holder.reply.setCount(comment.replyCount)
        holder.up.setCount(comment.upvote)
        holder.reply.setOnClickListener(onReplyClick)
        holder.date.setDatetime(second = comment.createdAt)
        holder.option.visibility = if (optionVisiable) View.VISIBLE else View.GONE
        holder.date.visibility = if (dateVisiable) View.VISIBLE else View.GONE
        holder.view.setOnClickListener(onClick)
        holder.content.maxLines = if (contentExpanded) Int.MAX_VALUE else 5
    }

    override fun buildView(parent: ViewGroup): View {
        return super.buildView(parent).also {

            // 每条评论里的图片都是一个 RecyclerView，那么我可以跨图片列表共享 view holder（在点评列表里）
            val pool = (parent as? RecyclerView)?.recycledViewPool
            it.findViewById<ImageTable>(R.id.imageTable).setRecycledViewPool(pool)
        }
    }



    class Holder: KotlinEpoxyHolder() {
        val avatar by bind<ImageView>(R.id.avatar)
        val name by bind<TextView>(R.id.nameTv)
        val ratingBar by bind<RatingBar>(R.id.ratingbar)
        val editorChoice by bind<EditorChoiceImageView>(R.id.editorChoiceIv)
        val content by bind<TextView>(R.id.contentTv)
        val imageTable by bind<ImageTable>(R.id.imageTable)
        val reply by bind<ReplyView>(R.id.reply)
        val up by bind<UpView>(R.id.up)
        val option by bind<View>(R.id.optionBtn)
        val date by bind<DateTextView>(R.id.dateTv)
    }
}