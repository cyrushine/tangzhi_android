package com.ifanr.tangzhi.ui.message.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer

import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.toast
import com.ifanr.tangzhi.ui.base.BaseViewModelFragment
import com.ifanr.tangzhi.ui.base.viewModelOf
import com.ifanr.tangzhi.ui.widgets.observeLoadingLiveData
import kotlinx.android.synthetic.main.message_page_fragment.*

/**
 * 我的消息列表
 */
class MessagePageFragment : BaseViewModelFragment() {

    companion object {
        private const val SYSTEM = "system"

        /**
         * @param system true - 系统消息列表
         */
        fun newInstance(system: Boolean = false) = MessagePageFragment().apply {
            if (system) {
                arguments = bundleOf(SYSTEM to SYSTEM)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.message_page_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val vm: MessagePageViewModel = viewModelOf()
        vm.error.observe(this, Observer { it?.also {
            toast(it)
        } })
        vm.pagedList.observe(this, Observer { it?.also {
            list.submitPagedList(it)
        } })
        vm.load(arguments?.getString(SYSTEM) == SYSTEM)
    }

}