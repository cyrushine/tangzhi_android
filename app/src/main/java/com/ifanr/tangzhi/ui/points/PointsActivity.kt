package com.ifanr.tangzhi.ui.points

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.route.Extra
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.statusBar
import kotlinx.android.synthetic.main.activity_points.*

/**
 * 我的积分
 */
@Route(path = Routes.points, extras = Extra.signIn)
class PointsActivity : BaseViewModelActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_points)
        statusBar(whiteText = false)

        card.setData(1826)
    }
}
