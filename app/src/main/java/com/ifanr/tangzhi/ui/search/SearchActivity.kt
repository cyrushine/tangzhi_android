package com.ifanr.tangzhi.ui.search

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.toast
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.search.widget.SearchBar
import com.ifanr.tangzhi.ui.search.widget.SearchList
import com.ifanr.tangzhi.ui.statusBar
import com.ifanr.tangzhi.ui.widgets.dismissLoading
import com.ifanr.tangzhi.ui.widgets.showLoading
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

@Route(path = Routes.search)
class SearchActivity : BaseViewModelActivity() {

    companion object {
        private const val TAG = "SearchActivity"
    }

    @Inject
    lateinit var repository: BaasRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        statusBar(whiteText = false)

        cancel.setOnClickListener { finish() }

        val vm: SearchViewModel = viewModel()
        vm.searchResult.observe(this, Observer { it?.also {
            list.setSearchResult(it)
        }})
        vm.searchHint.observe(this, Observer { it?.also {
            list.setSearchHint(it)
        }})
        vm.history.observe(this, Observer { it?.also {
            list.setIndexData(it, vm.hottest.value ?: emptyList())
        }})
        vm.hottest.observe(this, Observer { it?.also {
            list.setIndexData(vm.history.value ?: emptyList(), it)
        }})
        vm.toast.observe(this, Observer { it?.also { toast(it) } })
        vm.loading.observe(this, Observer {
            if (it == true)
                showLoading()
            else
                dismissLoading()
        })

        searchBar.listener = object: SearchBar.Listener {

            // 搜索框上的文本改变时，显示智能提示
            override fun onTextChanged(text: String) {
                vm.key.value = text
                vm.searchHint()
            }

            // 软键盘上的搜索按钮
            override fun onSearchClick(text: String) {
                vm.search()
            }

            // 点击清空搜索框按钮
            override fun onClearClick() {
                vm.loadIndexPage()
            }
        }

        list.listener = object : SearchList.Listener {

            // 点击智能提示
            override fun onHintClick(position: Int) {
                vm.searchHint.value?.getOrNull(position)?.let { it.name }?.also {
                    searchBar.setText(it)
                    vm.search()
                }
            }

            // 点击搜索历史
            override fun onHistoryClick(position: Int) {
                val key = vm.history.value?.getOrNull(position)
                if (!key.isNullOrEmpty()) {
                    searchBar.setText(key)
                    vm.search()
                }
            }

            // 点击热门推荐
            override fun onHottestClick(position: Int) {
                val key = vm.hottest.value?.getOrNull(position)
                if (!key.isNullOrEmpty()) {
                    searchBar.setText(key)
                    vm.search()
                }
            }

            // 点击清空搜索历史
            override fun onClearHistoryClick() {
                vm.clearHistory()
            }
        }

        vm.loadIndexPage()
    }
}
