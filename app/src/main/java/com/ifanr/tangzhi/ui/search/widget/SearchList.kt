package com.ifanr.tangzhi.ui.search.widget

import android.content.Context
import android.util.AttributeSet
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.ui.base.widget.AppEpoxyRV

class SearchList: AppEpoxyRV {

    interface Listener {
        /**
         * 点击智能提示条目
         */
        fun onHintClick(position: Int) {}
    }

    var listener: Listener = object : Listener {}

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
    }

    fun setSearchResult(list: PagedList<Product>) {
        val ctl = SearchResultController()
        setController(ctl)
        ctl.submitList(list)
        clearDecoration()
        addItemDecoration(SearchResultDecoration())
    }

    fun setSearchHint(list: List<Product>) {
        val ctl = SearchHintController()
        ctl.onHintClick = { listener.onHintClick(it) }
        setController(ctl)
        ctl.setData(list)
        clearDecoration()
        addItemDecoration(SearchHintDecoration(context))
    }

    fun setCategory(history: List<String>, hot: List<String>) {
        val ctl = SearchCategoryController()
        setController(ctl)
        ctl.setData(history, hot)
        clearDecoration()
    }


    private fun clearDecoration() {
        val count = itemDecorationCount
        repeat(count) { removeItemDecorationAt(0) }
    }
}