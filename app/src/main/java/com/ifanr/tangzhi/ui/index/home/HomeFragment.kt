package com.ifanr.tangzhi.ui.index.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.avatar
import com.ifanr.tangzhi.ui.base.BaseViewModelFragment
import com.ifanr.tangzhi.ui.base.viewModelOf
import com.ifanr.tangzhi.ui.widgets.RoundedRectDrawable
import kotlinx.android.synthetic.main.fragment_index_home.*

class HomeFragment: BaseViewModelFragment() {

    companion object {
        private const val TAG = "HomeFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_index_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val vm: HomeViewModel = viewModelOf()
        vm.products.observe(this, Observer { it?.also {
            homeProductList.setData(it)
        } })
    }
}