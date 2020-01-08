package com.ifanr.tangzhi.ui.share

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ext.delay
import com.ifanr.tangzhi.ui.base.BaseActivity
import com.ifanr.tangzhi.ui.statusBar
import kotlinx.android.synthetic.main.activity_share.*

@Route(path = Routes.share)
class ShareActivity : BaseActivity() {

    companion object {
        private const val TAG = "ShareActivity"
    }

    private val bottomSheetCallback = object: BottomSheetBehavior.BottomSheetCallback() {

        private var state = 0

        override fun onSlide(view: View, f: Float) {
            val percent = (1 + f).coerceIn(0f, 1f)
            if (!percent.isNaN() &&
                (state == BottomSheetBehavior.STATE_DRAGGING
                        || state == BottomSheetBehavior.STATE_SETTLING)) {
                background.alpha = percent
                Log.d(TAG, "$percent, $state")
            }
        }

        @SuppressLint("SwitchIntDef")
        override fun onStateChanged(view: View, state: Int) {
            this.state = state
            when (state) {
                BottomSheetBehavior.STATE_HIDDEN -> finish()
            }
        }
    }

    private lateinit var behavior: BottomSheetBehavior<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        statusBar(whiteText = true)

        behavior = BottomSheetBehavior.from(bottomPanel).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        delay(200) {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.setBottomSheetCallback(bottomSheetCallback)
            cancel.setOnClickListener { delay(100) { close() } }
        }
        background.animate()
            .setDuration(450)
            .alpha(1f)
            .start()
        background.setOnClickListener { close() }
    }

    override fun onBackPressed() {
        close()
    }

    private fun close() {
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
    }
}
