package com.ifanr.tangzhi.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.model.SearchKey
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.repository.baas.SettingRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import com.uber.autodispose.android.autoDispose
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val repository: BaasRepository,
    private val settingRepository: SettingRepository
): BaseViewModel() {

    companion object {
        private const val TAG = "SearchViewModel"
    }

    // 搜索关键字
    val key = MutableLiveData<String>()

    // 搜索结果
    val searchResult = MutableLiveData<PagedList<Product>>()

    // 智能提示列表
    val searchHint = MutableLiveData<List<Product>>()

    // 搜索历史
    val history = MutableLiveData<List<String>>()

    // 热门推荐
    val hottest = MutableLiveData<List<String>>()

    val toast = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    private var searchHintRequest: Disposable? = null

    // 加载搜索的首页
    fun loadIndexPage() {
        settingRepository.hottestSearch()
            .map { it.map { it.name ?: "" } }
            .subscribeOn(Schedulers.io())
            .autoDispose(this)
            .subscribe(Consumer {it?.also { hottest.postValue(it) }})

        repository.loadSearchLog()
            .map { it.map { it.key } }
            .subscribeOn(Schedulers.io())
            .autoDispose(this)
            .subscribe(Consumer { it?.also { history.postValue(it) } })
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
                .doOnSubscribe { loading.postValue(true) }
                .doAfterTerminate { loading.postValue(false) }
                .autoDispose(this)
                .subscribe(Consumer { searchResult.value = it })
        }
    }

    // 清空搜索历史
    fun clearHistory() {
        repository.cleanSearchLog()
            .subscribeOn(Schedulers.io())
            .autoDispose(this)
            .subscribe({ loadIndexPage() }, { toast.postValue(it.message) })
    }

    // 智能提示
    fun searchHint() {
        val text = key.value
        if (!text.isNullOrEmpty()) {
            searchHintRequest?.dispose()
            searchHintRequest = repository.searchHint(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDispose(this)
                .subscribe(Consumer { it.takeIf { !it.isNullOrEmpty() }?.also {
                    searchHint.value = it
                }})
        }
    }
}