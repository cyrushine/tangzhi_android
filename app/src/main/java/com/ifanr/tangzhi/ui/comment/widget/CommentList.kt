package com.ifanr.tangzhi.ui.comment.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import com.ifanr.tangzhi.model.Comment
import com.ifanr.tangzhi.ui.base.widget.AppEpoxyRV

class CommentList: AppEpoxyRV {

    interface Listener {
        fun onReplyClick(id: String) {}
        fun onVoteClick(id: String) {}
        fun onOptionClick(id: String) {}
        fun onLoadMore() {}

        /**
         * 二级评论，点击加载更多
         * @param id 是 ChildLoadMore 的 id，用来搜索
         */
        fun onChildLoadMoreClick(id: String) {}
    }

    private val controller: Controller

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        controller = Controller()
        setController(controller)
        addItemDecoration(ItemDecorationImpl(context))
    }

    fun setData(data: List<Comment>) {
        controller.setData(data)
    }

    fun setListener(l: Listener) {
        controller.listener = l
    }
}

