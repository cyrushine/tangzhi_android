package com.ifanr.tangzhi.ui.comment.model

import com.ifanr.tangzhi.model.Comment
import com.ifanr.tangzhi.util.uuid

class ChildLoadMore: Comment() {

    var loading = false

    init {
        id = uuid()
    }
}