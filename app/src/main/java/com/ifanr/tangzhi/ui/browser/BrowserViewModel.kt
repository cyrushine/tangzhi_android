package com.ifanr.tangzhi.ui.browser

import androidx.lifecycle.MutableLiveData
import com.ifanr.tangzhi.ui.base.BaseViewModel
import javax.inject.Inject

class BrowserViewModel @Inject constructor(): BaseViewModel() {

    /**
     * 只用来保存当前页面的地址，不导致 web view 加载
     */
    val url = MutableLiveData<String>()

    val title = MutableLiveData<String>()

    /**
     * 0 - 100
     */
    val progress = MutableLiveData<Int>()
}