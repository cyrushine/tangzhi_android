package com.ifanr.tangzhi.ui.index.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MenuItem
import androidx.core.view.children
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getColorCompat
import com.ifanr.tangzhi.ui.icons.IconDrawable

class IndexBottomMenu: BottomNavigationView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        menu.findItem(R.id.home).apply {
            icon = IconDrawable(iconRes = R.string.ic_home_tabbar, ctx = context)

        }

        menu.findItem(R.id.profile).apply {
            icon = IconDrawable(iconRes = R.string.ic_me_tabbar, ctx = context)
        }
    }

    fun bind(vp: ViewPager) {
        setOnNavigationItemSelectedListener {
            val pagerIndex = when (it.itemId) {
                R.id.home -> 0
                R.id.profile -> 1
                else -> -1
            }
            if (pagerIndex >= 0 && vp.currentItem != pagerIndex) {
                vp.currentItem = pagerIndex
            }
            true
        }

        vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                val itemId = when (position) {
                    0 -> R.id.home
                    1 -> R.id.profile
                    else -> -1
                }
                if (itemId > 0 && selectedItemId != itemId) {
                    selectedItemId = itemId
                }
            }
        })
    }
}