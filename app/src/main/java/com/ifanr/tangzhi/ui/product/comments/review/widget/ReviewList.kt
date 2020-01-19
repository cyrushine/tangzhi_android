package com.ifanr.tangzhi.ui.product.comments.review.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.*
import com.bumptech.glide.Glide
import com.ifanr.tangzhi.Const
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.avatar
import com.ifanr.tangzhi.model.Comment
import com.ifanr.tangzhi.ui.base.epoxy.KotlinEpoxyHolder
import com.ifanr.tangzhi.ui.base.model.BaseTypedController
import com.ifanr.tangzhi.ui.base.model.LoadMoreAwaredController
import com.ifanr.tangzhi.ui.base.widget.AppEpoxyRV
import com.ifanr.tangzhi.ui.widgets.*

/**
 * 点评列表
 */
class ReviewList: AppEpoxyRV {

    companion object {
        val VOTE_CHANGED = Any()
        val REPLY_CHANGED = Any()
        private const val TAG = "ReviewList"
    }

    interface Listener {
        fun onReplyClick(position: Int) {}
        fun onClick(position: Int) {}
        fun onVoteClick(position: Int) {}
    }

    private val controller =
        ProductReviewController()


    private val listUpdateLogger = OnModelBuildFinishedListener {
        it.dispatchTo(object : ListUpdateCallback {
            override fun onChanged(position: Int, count: Int, payload: Any?) {
                Log.d(TAG, "onChanged, position($position), count: $count")
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                Log.d(TAG, "onMoved, from $fromPosition to $toPosition")
            }

            override fun onInserted(position: Int, count: Int) {
                Log.d(TAG, "onInserted, position: $position, count: $count")
            }

            override fun onRemoved(position: Int, count: Int) {
                Log.d(TAG, "onRemoved, position: $position, count: $count")
            }
        })
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        setRecycledViewPool(RecycledViewPool())
        setController(controller)
        controller.addModelBuildListener(listUpdateLogger)
    }

    fun setData(data: List<Comment>, scrollTop: Boolean = false) {
        controller.setData(data)
        if (scrollTop)
            scrollToPosition(0)
    }

    fun setLoadMoreListener(l: () -> Unit) {
        controller.loadMoreListener = l
    }

    fun setListener(l: Listener) {
        controller.listener = l
    }
}

class ProductReviewController: LoadMoreAwaredController<List<Comment>>() {

    var listener: ReviewList.Listener = object: ReviewList.Listener {}

    override fun buildModels(data: List<Comment>?) {
        data?.forEach {
            productReview {
                id(it.id)
                comment(it)
                onReplyClick { _, _, _, position ->
                    listener.onReplyClick(position)
                }
                onClick { _, _, _, position ->
                    listener.onClick(position)
                }
                onVoteClick { _, _, _, position ->
                    listener.onVoteClick(position)
                }
            }
        }
    }
}

/**
 * 点评样式
 */
@EpoxyModelClass(layout = R.layout.product_comment)
abstract class ProductReviewModel: EpoxyModelWithHolder<ProductReviewModel.Holder>() {

    companion object {
        private const val TAG = "ProductReviewModel"
    }

    @EpoxyAttribute
    lateinit var comment: Comment

    // 回复按钮的点击
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onReplyClick: View.OnClickListener

    // 整个点评的点击
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onClick: View.OnClickListener

    // 「有用」按钮的点击
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onVoteClick: View.OnClickListener

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
        holder.up.isSelected = comment.voted
        holder.up.setOnClickListener(onVoteClick)
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
            it.findViewById<ImageTable>(
                R.id.imageTable
            ).setRecycledViewPool(pool)
        }
    }



    class Holder: KotlinEpoxyHolder() {
        val avatar by bind<ImageView>(R.id.avatar)
        val name by bind<TextView>(R.id.nameTv)
        val ratingBar by bind<RatingBar>(
            R.id.ratingbar
        )
        val editorChoice by bind<EditorChoiceImageView>(
            R.id.editorChoiceIv
        )
        val content by bind<TextView>(R.id.contentTv)
        val imageTable by bind<ImageTable>(
            R.id.imageTable
        )
        val reply by bind<ReplyView>(
            R.id.reply
        )
        val up by bind<UpView>(R.id.up)
        val option by bind<View>(R.id.optionBtn)
        val date by bind<DateTextView>(
            R.id.dateTv
        )
    }
}