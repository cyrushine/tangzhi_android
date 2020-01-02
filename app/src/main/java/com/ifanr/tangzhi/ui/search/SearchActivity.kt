package com.ifanr.tangzhi.ui.search

import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.Routes
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.search.widget.SearchBar
import com.ifanr.tangzhi.ui.search.widget.SearchList
import com.ifanr.tangzhi.ui.statusBar
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
        vm.hotAndHistory.observe(this, Observer { it?.also {
            list.setCategory(it.first, it.second)
        } })

        searchBar.listener = object: SearchBar.Listener {
            override fun onTextChanged(text: String) {
                vm.key.value = text.trim()
            }

            override fun onSearchClick(text: String) {
                vm.search()
            }
        }

        list.listener = object : SearchList.Listener {
            override fun onHintClick(position: Int) {
                vm.searchHint.value?.getOrNull(position)?.let { it.name }?.also {
                    searchBar.setText(it)
                    vm.search()
                }
            }
        }
        vm.loadIndexPage()
    }
}
