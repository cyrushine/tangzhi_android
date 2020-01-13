package com.ifanr.tangzhi.ui.points

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.ifanr.tangzhi.model.PointLog
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PointsViewModel @Inject constructor(
    private val repository: BaasRepository
): BaseViewModel() {

    val records = MediatorLiveData<PagedList<PointLog>>()

    val type = MutableLiveData<String>()

    val userPoints = MutableLiveData<Int>()

    init {
        records.addSource(type) {
            repository.pointLogList(type = it)
                .subscribeOn(Schedulers.io())
                .autoDispose(this)
                .subscribe(Consumer { records.postValue(it) })
        }
        type.value = ""

        repository.loadUserProfile()
            .subscribeOn(Schedulers.io())
            .autoDispose(this)
            .subscribe(Consumer { userPoints.postValue(it.point) })
    }
}