package com.ifanr.tangzhi.ui.comment.widget.model

import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.epoxy.KotlinEpoxyHolder
import com.ifanr.tangzhi.ui.comment.model.ChildLoadMore
import com.ifanr.tangzhi.ui.comment.widget.LoadMoreTextView

@EpoxyModelClass(layout = R.layout.comment_child_load_more)
abstract class ChildLoadMoreModel: EpoxyModelWithHolder<ChildLoadMoreModel.Holder>() {

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onClick: View.OnClickListener

    @EpoxyAttribute
    lateinit var state: ChildLoadMore

    override fun bind(holder: Holder) {
        holder.text.state = if (state.loading)
            LoadMoreTextView.State.LOADING
        else
            LoadMoreTextView.State.IDLE

        if (!state.loading) {
            holder.text.setOnClickListener {
                state.loading = true
                holder.text.state = LoadMoreTextView.State.LOADING
                onClick.onClick(it)
            }
        }
    }

    class Holder: KotlinEpoxyHolder() {
        val text by lazy { view as LoadMoreTextView }
    }
}