package com.ifanr.tangzhi.ui.message

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.alibaba.android.arouter.facade.annotation.Route
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.route.Extra
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.message.fragment.MessagePageFragment
import com.ifanr.tangzhi.ui.statusBar
import kotlinx.android.synthetic.main.activity_message.*

/**
 * 我的消息
 */
@Route(path = Routes.message, extras = Extra.signIn)
class MessageActivity : BaseViewModelActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        statusBar(whiteText = false)

        toolBar.close.setOnClickListener { finish() }
        vp.adapter = MessageAdapter(fm = supportFragmentManager, ctx = this)
        tabLayout.setupWithViewPager(vp)
    }
}

class MessageAdapter(
    fm: FragmentManager,
    private val ctx: Context
): FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MessagePageFragment.newInstance()
            else -> MessagePageFragment.newInstance(system = true)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> ctx.getString(R.string.message_type_normal)
            else -> ctx.getString(R.string.message_type_system)
        }
    }

    override fun getCount(): Int = 2
}