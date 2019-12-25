package com.ifanr.tangzhi.ui.product.comments.review.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ifanr.tangzhi.ui.product.comments.discussion.DiscussionFragment
import com.ifanr.tangzhi.ui.product.comments.review.ReviewFragment

class CommentViewPager: ViewPager {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {
        adapter = CommentAdapter((context as AppCompatActivity).supportFragmentManager)
    }
}

private class CommentAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ReviewFragment()
            else -> DiscussionFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }
}