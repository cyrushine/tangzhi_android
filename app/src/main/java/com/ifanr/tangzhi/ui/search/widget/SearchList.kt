package com.ifanr.tangzhi.ui.search.widget

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.InputMethodManager
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

        // 清空搜索历史
        fun onClearHistoryClick() {}

        // 点击搜索历史条目
        fun onHistoryClick(position: Int) {}

        // 点击热门推荐条目
        fun onHottestClick(position: Int) {}
    }

    private val imm by lazy {
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }
    var listener: Listener = object : Listener {}
    private val indexCtlListener = object: SearchIndexController.Listener {

        override fun onClearHistoryClick() {
            listener.onClearHistoryClick()
        }

        override fun onHistoryClick(position: Int) {
            imm.hideSoftInputFromWindow(windowToken, 0)
            listener.onHistoryClick(position)
        }

        override fun onHottestClick(position: Int) {
            imm.hideSoftInputFromWindow(windowToken, 0)
            listener.onHottestClick(position)
        }

    }
    private val onHintClick: (Int) -> Unit = {
        imm.hideSoftInputFromWindow(windowToken, 0)
        listener.onHintClick(it)
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
    }

    fun setSearchResult(list: PagedList<Product>) {
        val ctl = SearchResultController()
        setController(ctl)
        ctl.submitList(list)
        clearDecoration()
        addItemDecoration(SearchResultDecoration())
    }

    fun setSearchHint(list: List<Product>) {
        val ctl = SearchHintController(onHintClick)
        setController(ctl)
        ctl.setData(list)
        clearDecoration()
        addItemDecoration(SearchHintDecoration(context))
    }

    fun setIndexData(history: List<String>, hottest: List<String>) {
        val ctl = SearchIndexController(indexCtlListener)
        setController(ctl)
        ctl.setData(history, hottest)
        clearDecoration()
        addItemDecoration(SearchIndexDecoration())
    }

    private fun clearDecoration() {
        val count = itemDecorationCount
        repeat(count) { removeItemDecorationAt(0) }
    }
}