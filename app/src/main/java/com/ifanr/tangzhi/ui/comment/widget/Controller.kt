package com.ifanr.tangzhi.ui.comment.widget

import com.airbnb.epoxy.AutoModel
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.model.Comment
import com.ifanr.tangzhi.ui.base.model.LoadMoreAwaredController
import com.ifanr.tangzhi.ui.comment.model.ChildLoadMore
import com.ifanr.tangzhi.ui.comment.widget.model.*
import com.ifanr.tangzhi.ui.product.comments.review.widget.productReview

class Controller: LoadMoreAwaredController<List<Comment>>() {

    var listener: CommentList.Listener = object:
        CommentList.Listener {}

    @AutoModel
    lateinit var reviewTitle: CommentReviewTitleModel_

    @AutoModel
    lateinit var allTitle: CommentAllTitleModel_

    init {
        loadMoreListener = { listener.onLoadMore() }
    }

    override fun buildModels(data: List<Comment>?) {
        if (!data.isNullOrEmpty()) {
            add(reviewTitle)

            val review = data.first()
            productReview {
                id(review.id)
                onClick { _ -> }
                onReplyClick { _, _, _, _ ->
                    listener.onReplyClick(review.id)
                }
                onVoteClick { _, _, _, _ ->
                    listener.onVoteClick(review.id)
                }
                comment(review)
                dateVisiable(true)
                optionVisiable(true)
                contentExpanded(true)
            }

            add(allTitle)
            data.takeLast(data.size - 1).forEach {
                commentItem {
                    id(it.id)
                    data(it)
                    onReplyClick { _, _, _, _ ->
                        listener.onReplyClick(it.id)
                    }
                    onUpClick { _, _, _, _ ->
                        listener.onVoteClick(it.id)
                    }
                    onOptionClick { _, _, _, _ ->
                        listener.onOptionClick(it.id)
                    }
                }

                val children = it.children
                if (children.size == 2 && children[1] is ChildLoadMore) {
                    val foldChild = children[0]
                    val loadMore = children[1] as ChildLoadMore
                    childFold {
                        id(foldChild.id)
                        data(foldChild)
                    }
                    childLoadMore {
                        id(loadMore.id)
                        state(loadMore)
                        onClick { _ -> listener.onChildLoadMoreClick(loadMore.id) }
                    }

                } else if (children.size == 1) {
                    val single = children[0]
                    child {
                        id(single.id)
                        data(single)
                        background(R.drawable.comment_child_single)
                        onReplyClick { _, _, _, _ ->
                            listener.onReplyClick(single.id)
                        }
                        onUpClick { _, _, _, _ ->
                            listener.onVoteClick(single.id)
                        }
                        onOptionClick { _, _, _, _ ->
                            listener.onOptionClick(single.id)
                        }
                    }

                } else {
                    children.forEachIndexed { index, child ->
                        child {
                            id(child.id)
                            data(child)
                            background(when (index) {
                                0 -> R.drawable.comment_child_top
                                children.lastIndex -> R.drawable.comment_child_bottom
                                else -> R.drawable.comment_child_middle
                            })
                            onReplyClick { _, _, _, _ ->
                                listener.onReplyClick(child.id)
                            }
                            onUpClick { _, _, _, _ ->
                                listener.onVoteClick(child.id)
                            }
                            onOptionClick { _, _, _, _ ->
                                listener.onOptionClick(child.id)
                            }
                        }
                    }
                }
            }
        }
    }
}