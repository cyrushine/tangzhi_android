package com.ifanr.tangzhi.ui.message.fragment

import android.content.Context
import android.graphics.Rect
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.TextAppearanceSpan
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.avatar
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.model.Comment
import com.ifanr.tangzhi.model.Message
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseItemDecoration
import com.ifanr.tangzhi.ui.base.epoxy.KotlinEpoxyHolder
import com.ifanr.tangzhi.ui.base.model.BasePagedListController
import com.ifanr.tangzhi.ui.base.widget.AppEpoxyRV
import com.ifanr.tangzhi.ui.widgets.DateTextView
import com.ifanr.tangzhi.ui.widgets.MessageProductCard

class MessageList: AppEpoxyRV {

    private val ctl by lazy { MessageController() }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setController(ctl)
        addItemDecoration(Decoration())
    }

    fun submitPagedList(list: PagedList<Message>) {
        ctl.submitPagedList(list)
        ctl.requestModelBuild()
    }
}

class MessageController: BasePagedListController<Message>() {

    override fun buildItemModel(currentPosition: Int, item: Message?): EpoxyModel<*> {
        val data = item ?: Message()
        return MessageModel_().apply {
            id(data.id)
            data(data)
        }
    }
}

@EpoxyModelClass(layout = R.layout.message_model)
abstract class MessageModel: EpoxyModelWithHolder<MessageModel.Holder>() {

    @EpoxyAttribute
    lateinit var data: Message

    override fun bind(holder: Holder) {
        val product = data.detail?.product
        val comment = data.detail?.comment
        val root = data.detail?.rootComment
        val parent = data.detail?.parentComment
        val reply = data.detail?.replyComment
        val participant = data.detail?.participant
        val sender = data.sender
        when (data.type) {

            // 普通消息
            Message.TYPE_PRODUCT, Message.TYPE_TESTING -> {
                holder.nickname.visibility = View.VISIBLE
                holder.action.visibility = View.VISIBLE
                holder.systemName.visibility = View.GONE
                Glide.with(holder.avatar).avatar().load(sender?.displayAvatar).into(holder.avatar)
                holder.nickname.text = sender?.displayName

                // 对于产品、众测和讨论，都分为三种情况：发表点评、发表评论和回复评论
                val commentAction = when {
                    parent != null -> CommentAction.REPLY_COMMENT
                    root != null -> CommentAction.SEND_COMMENT
                    else -> CommentAction.SEND_REVIEW
                }
                holder.action.setText(when {
                    data.action == Message.ACTION_QUESTION -> R.string.message_action_answer
                    commentAction == CommentAction.SEND_REVIEW -> R.string.message_action_send_review
                    commentAction == CommentAction.SEND_COMMENT -> R.string.message_action_reply_review
                    else -> R.string.message_action_reply_comment
                })

                holder.content.text = comment?.content
                holder.card.setContent(
                    when (commentAction) {
                        CommentAction.REPLY_COMMENT -> reply?.content ?: ""
                        CommentAction.SEND_COMMENT -> root?.content ?: ""
                        else -> product?.name ?: ""
                    }
                )
                holder.card.setName(product?.name ?: "")
            }

            // 系统消息
            else -> {
                holder.nickname.visibility = View.GONE
                holder.action.visibility = View.GONE
                holder.systemName.visibility = View.VISIBLE
                Glide.with(holder.avatar).avatar().load(R.drawable.system_avatar).into(holder.avatar)

                val productName = product?.name ?: ""
                var cardContent: String
                var content: CharSequence = ""

                cardContent = comment?.content ?: ""
                if (cardContent.isEmpty())
                    cardContent = product?.description ?: ""

                when (data.action) {
                    Message.ACTION_RECOMMEND -> {
                        content = holder.ctx.getString(R.string.message_content_recommend)
                    }

                    Message.ACTION_UPVOTES -> {
                        val userList = participant?.joinToString(separator = "、") ?: ""
                        val userCount = participant?.size?.toString() ?: "0"

                        content = SpannableStringBuilder().apply {
                            append(holder.ctx.getString(
                                if (comment?.type == Comment.TYPE_REVIEW)
                                    R.string.message_content_upvotes_review
                                else
                                    R.string.message_content_upvotes_comment,
                                userList,
                                userCount
                            ))
                            setSpan(
                                TextAppearanceSpan(holder.ctx, R.style.MessageUpVoteList),
                                0,
                                userList.length,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                    }

                    Message.ACTION_ANSWER_INVATION -> {
                        content = holder.ctx.getString(R.string.message_content_answer_invation)
                    }
                }

                holder.card.setContent(cardContent)
                holder.card.setName(productName)
                holder.content.text = content
            }
        }

        holder.date.setDatetime(data.createdAt)

        val productId = product?.id
        holder.card.setOnClickListener {
            if (productId != null) {
                ARouter.getInstance().build(Routes.product)
                    .withString(Routes.productId, productId)
                    .navigation(holder.ctx)
            }
        }
    }

    class Holder: KotlinEpoxyHolder() {
        val avatar by bind<ImageView>(R.id.avatar)
        val date by bind<DateTextView>(R.id.dateTv)
        val nickname by bind<TextView>(R.id.nicknameTv)
        val action by bind<TextView>(R.id.actionTv)
        val systemName by bind<TextView>(R.id.systemNameTv)
        val content by bind<TextView>(R.id.contentTv)
        val card by bind<MessageProductCard>(R.id.card)
    }
}


class Decoration: BaseItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        vh: RecyclerView.ViewHolder,
        vt: Int,
        position: Int
    ) {
        if (vt == R.layout.message_model) {
            outRect.top = view.context.dp2px(if (position == 0) 20 else 40)
        }
    }
}


private enum class CommentAction {
    SEND_REVIEW,
    SEND_COMMENT,
    REPLY_COMMENT
}