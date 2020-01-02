package com.ifanr.tangzhi.ui.index.home

import androidx.lifecycle.MutableLiveData
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: BaasRepository
): BaseViewModel() {

    // 最新的 20 个产品
    val products = object: MutableLiveData<List<Product>>() {
        override fun onActive() {
            repository.latestProduct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDispose(this@HomeViewModel)
                .subscribe(Consumer { value = it })
        }
    }


}