package com.ifanr.tangzhi.ui.timeline

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.model.Timeline
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseItemDecoration
import com.ifanr.tangzhi.ui.base.epoxy.KotlinEpoxyHolder
import com.ifanr.tangzhi.ui.base.model.BasePagedListController
import com.ifanr.tangzhi.ui.base.widget.AppEpoxyRV
import com.ifanr.tangzhi.ui.widgets.DateTextView
import com.ifanr.tangzhi.ui.widgets.MessageProductCard

class TimelineList: AppEpoxyRV {

    private val ctl = TimelineController()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setController(ctl)
        addItemDecoration(TimelineDecoration(context))
    }

    fun submitPagedList(list: PagedList<Timeline>) {
        ctl.submitPagedList(list)
    }

}

class TimelineController: BasePagedListController<Timeline>() {
    override fun buildItemModel(currentPosition: Int, item: Timeline?): EpoxyModel<*> {
        val data = item ?: Timeline()
        return TimelineModel_().apply {
            id(data.id)
            data(data)
        }
    }
}


@EpoxyModelClass(layout = R.layout.timeline_model)
abstract class TimelineModel: EpoxyModelWithHolder<TimelineModel.Holder>() {

    @EpoxyAttribute
    lateinit var data: Timeline

    override fun bind(holder: Holder) {

        holder.actionTv.setText(when (data.action) {
            Timeline.ACTION_ANSWER -> R.string.timeline_action_answer
            Timeline.ACTION_QUESTION -> R.string.timeline_action_question
            Timeline.ACTION_DISCUSSION -> R.string.timeline_action_discussion
            else -> R.string.timeline_action_review
        })

        holder.dateTv.setDatetime(data.createdAt)
        holder.contentTv.text = data.detail?.comment?.content
        holder.card.setContent(data.detail?.product?.description ?: "")
        holder.card.setName(data.detail?.product?.name ?: "")

        val productId = data.detail?.product?.id
        holder.card.setOnClickListener {
            if (!productId.isNullOrEmpty()) {
                ARouter.getInstance().build(Routes.product)
                    .withString(Routes.productId, productId)
                    .navigation(holder.ctx)
            }
        }
    }

    class Holder: KotlinEpoxyHolder() {
        val actionTv by bind<TextView>(R.id.actionTv)
        val dateTv by bind<DateTextView>(R.id.dateTv)
        val contentTv by bind<TextView>(R.id.contentTv)
        val card by bind<MessageProductCard>(R.id.card)
    }
}

class TimelineDecoration (ctx: Context): BaseItemDecoration() {

    private val padding = ctx.dp2px(20)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        vh: RecyclerView.ViewHolder,
        vt: Int,
        position: Int
    ) {
        if (vt == R.layout.timeline_model) {
            outRect.left = padding
            outRect.right = padding
            outRect.top = if (position == 0) padding else padding * 2
        }
    }
}