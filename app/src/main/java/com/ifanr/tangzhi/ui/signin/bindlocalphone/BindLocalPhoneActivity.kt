package com.ifanr.tangzhi.ui.signin.bindlocalphone

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.*
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.statusBar
import com.ifanr.tangzhi.ui.widgets.observeLoadingLiveData
import kotlinx.android.synthetic.main.activity_bind_local_phone.*
import javax.inject.Inject

/**
 * 绑定本机手机号
 */
@Route(path = Routes.bindLocalPhone)
class BindLocalPhoneActivity : BaseViewModelActivity() {

    companion object {
        private val PERMISSIONS = arrayOf(Manifest.permission.READ_PHONE_STATE)
        private const val GRANT_PHONE = 4
        private const val TAG = "BindLocalPhoneActivity"
    }

    @Inject
    lateinit var tm: TelephonyManager
    lateinit var vm: BindLocalPhoneViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bind_local_phone)
        statusBar(whiteText = false)
        vm = viewModel()
        observeLoadingLiveData(vm.loading)
        vm.phone.observe(this, Observer {
            phone.text = if (it.isNullOrEmpty()) "" else
                getString(R.string.bind_local_phone_number, it)
        })
        vm.event.observe(this, Observer {
            when (it) {
                Event.PhoneNotFound -> {
                    toast(R.string.bind_local_phone_not_found)
                    bindOther.performClick()
                }

                Event.SignInSuccess -> {
                    finish()
                }

                is Event.SignInFail -> {
                    toast("${getString(R.string.bind_local_phone_fail)}(${it.ex.message})")
                }
            }
        })

        toolBar.close.setOnClickListener { finish() }

        bind.setOnClickListener {
            if (!checkPermissions(PERMISSIONS)) {
                GrantPermissionDialog(ctx = this) {
                    requestPermissionCompat(PERMISSIONS, GRANT_PHONE)
                }
            } else {
                signInQuick()
            }
        }

        bindOther.setOnClickListener {
            ARouter.getInstance().build(Routes.bindPhone)
                .withString(Routes.bindPhoneType, "wechat")
                .navigation(this)
            finishDelay()
        }
    }

    /**
     * 一键登录
     */
    private fun signInQuick() {
        vm.signInQuick()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            GRANT_PHONE -> {
                if (grantResults.permissionGranted()) {
                    signInQuick()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}

/**
 * 获取本机号码的授权弹窗
 */
private fun GrantPermissionDialog(ctx: Context, ok: () -> Unit) {
    AlertDialog.Builder(ctx)
        .setTitle(R.string.bind_local_phone_grant_title)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setMessage(R.string.bind_local_phone_grant_tip)
        .setPositiveButton(R.string.bind_local_phone_grant_ok) { dialog, _ ->
            ok.invoke()
            dialog.dismiss()
        }
        .setNeutralButton(R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        .show()
}