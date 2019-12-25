package com.ifanr.tangzhi.ui.product.comments.discussion

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ifanr.tangzhi.R

class DiscussionFragment : Fragment() {

    companion object {
        fun newInstance() =
            DiscussionFragment()
    }

    private lateinit var viewModel: DiscussionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.discussion_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DiscussionViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
