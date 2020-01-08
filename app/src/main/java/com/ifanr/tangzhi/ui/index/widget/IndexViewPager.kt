package com.ifanr.tangzhi.ui.index.widget

import android.content.Context
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ifanr.tangzhi.ui.index.home.HomeFragment
import com.ifanr.tangzhi.ui.index.profile.ProfileFragment

class IndexViewPager: ViewPager {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    fun init(fm: FragmentManager) {
        adapter = IndexAdapter(fm = fm)
    }
}

private class IndexAdapter (fm: FragmentManager): FragmentStatePagerAdapter(
    fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    private val pagers = listOf(HomeFragment(), ProfileFragment())

    override fun getItem(position: Int): Fragment = pagers[position]

    override fun getCount(): Int = pagers.size
}