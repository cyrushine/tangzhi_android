package com.ifanr.tangzhi.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.model.SearchKey
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val repository: BaasRepository
): BaseViewModel() {

    companion object {
        private const val TAG = "SearchViewModel"
    }

    // 搜索框里的文本
    val key = MutableLiveData<String>()

    // 搜索结果
    val searchResult = MutableLiveData<PagedList<Product>>()

    // 智能提示列表
    val searchHint = MutableLiveData<List<Product>>()

    // 搜索历史和热门推荐
    val hotAndHistory = MutableLiveData<Pair<List<String>, List<String>>>()

    private var searchHintRequest: Disposable? = null

    init {

        // 这里监听文本的改变（debounce），查询智能提示列表
        Observable.create<String> { emitter ->
            key.observeForever { it.takeIf { !it.isNullOrEmpty() }?.also { text ->
                emitter.onNext(text)
            }}}
            .debounce(500L, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(this)
            .subscribe {
                searchHintRequest?.dispose()
                searchHintRequest = repository.searchHint(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .autoDispose(this)
                    .subscribe(Consumer { it.takeIf { !it.isNullOrEmpty() }?.also {
                        searchHint.value = it
                    }})
            }
    }

    fun loadIndexPage() {
        repository.searchHotKeys()
            .map { it.map { it.name ?: "" } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(this)
            .subscribe(Consumer {
                hotAndHistory.value = emptyList<String>() to (it ?: emptyList())
            })

    }

    /**
     * 搜索
     */
    fun search() {
        val text = key.value
        if (!text.isNullOrEmpty()) {
            searchHintRequest?.dispose()
            searchHintRequest = null
            repository.search(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDispose(this)
                .subscribe(Consumer { searchResult.value = it })
        }
    }
}