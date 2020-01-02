package com.ifanr.tangzhi.ui.postlist

import androidx.lifecycle.MutableLiveData
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PostListViewModel @Inject constructor(
    private val repository: BaasRepository
): BaseViewModel() {

    val list = MutableLiveData<List<Product.CachedPost>>()

    fun load(productId: String) {
        repository.getProductById(productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(this)
            .subscribe(Consumer { list.value = it.cachedPost })
    }

}