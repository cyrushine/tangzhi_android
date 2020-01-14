package com.ifanr.tangzhi.ui.share

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ifanr.tangzhi.Const
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ext.delay
import com.ifanr.tangzhi.ui.base.BaseActivity
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.statusBar
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject
import com.tencent.mm.opensdk.openapi.IWXAPI
import kotlinx.android.synthetic.main.activity_share.*
import javax.inject.Inject
import javax.inject.Provider

@Route(path = Routes.share)
class ShareActivity : BaseViewModelActivity() {

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

    @Autowired(name = Routes.shareObject, required = false)
    @JvmField
    var shareObject: Parcelable? = null

    lateinit var vm: ShareViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        statusBar(whiteText = true)
        vm = viewModel()

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
        wechat.setOnClickListener { onWechatClick() }
    }

    override fun onBackPressed() {
        close()
    }

    private fun close() {
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    /**
     * 点击分享至微信
     */
    private fun onWechatClick() {
        val shared = shareObject
        when (shared) {
            is ShareProductReq -> vm.shareProductToWechat(shared)
        }
        close()
    }
}
