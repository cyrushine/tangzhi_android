package com.ifanr.tangzhi.ui.updateprofile

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.*
import com.ifanr.tangzhi.route.Extra
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.statusBar
import com.ifanr.tangzhi.ui.widgets.LoadingDialogFragment
import com.uber.autodispose.android.lifecycle.autoDispose
import com.uber.autodispose.autoDispose
import com.zhihu.matisse.Matisse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_update_profile.*

/**
 * 编辑个人资料
 */
@Route(path = Routes.updateProfile, extras = Extra.signIn)
class UpdateProfileActivity : BaseViewModelActivity() {

    companion object {
        private const val OPEN_IMAGE_PICKER = 7
        private const val PICK_IMAGE = 4
        private val PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        private const val LOADING_TAG = "loading"
        private const val TAG = "UpdateProfileActivity"
    }

    lateinit var vm: UpdateProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        statusBar(whiteText = false)

        toolBar.close.setOnClickListener { close() }
        avatarItem.setOnClickListener { openImagePicker() }

        vm = viewModel()

        // loading 和 toast
        vm.loading.observe(this, Observer {
            val loading = supportFragmentManager.findFragmentByTag(LOADING_TAG)
            if (it == true) {
                if (loading == null) {
                    LoadingDialogFragment.show(supportFragmentManager, tag = LOADING_TAG)
                }
            } else {
                if (loading != null) {
                    supportFragmentManager.beginTransaction().remove(loading).commitNowAllowingStateLoss()
                }
            }
        })
        vm.toast.observe(this, Observer { it?.also { toast(it) }})


        // banner
        vm.banners.observe(this, Observer { it?.also {
            banners.setData(it, vm.profileForm.value?.banner)
        }})
        vm.profileForm.observe(this, Observer { it?.also {
            banners.setData(vm.banners.value ?: emptyList(), it.banner)
        }})
        banners.setListener(object: BannerList.Listener {
            override fun onItemClick(position: Int) {
                vm.onBannerSelected(position)
            }
        })


        // 普通字段
        nicknameValue.doAfterTextChanged {
            vm.profileForm.value = vm.profileForm.value?.copy(displayName = it?.toString() ?: "")
        }
        mottoValue.doAfterTextChanged {
            vm.profileForm.value = vm.profileForm.value?.copy(motto = it?.toString() ?: "")
        }
        professionValue.doAfterTextChanged {
            vm.profileForm.value = vm.profileForm.value?.copy(profession = it?.toString() ?: "")
        }
        addressValue.doAfterTextChanged {
            vm.profileForm.value = vm.profileForm.value?.copy()
        }
        vm.currentProfile.observe(this, Observer { it?.also {
            nicknameValue.setText(it.displayName)
            mottoValue.setText(it.motto)
            professionValue.setText(it.profession)
            phoneValue.setPhone(it.phone)
            addressValue.setText(it.address)
        }})

        // 如果没有绑定手机号，则进入绑定页面
        phoneValue.setOnClickListener {
            if (vm.currentProfile.value?.phone.isNullOrEmpty()) {
                ARouter.getInstance().build(Routes.bindPhone)
                    .withString(Routes.bindPhoneType, "wechat")
                    .navigation(this)
            }
        }


        // 头像
        vm.profileForm.observe(this, Observer { it?.also {
            Glide.with(this).avatar().load(it.displayAvatar).into(avatarValue)
        }})

        // 保存
        save.setOnClickListener {
            vm.save()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDispose(this)
                .subscribe({
                    toast(R.string.update_profile_success)
                    finish()
                }, {
                    Log.e(TAG, it.message, it)
                    toastFail(it.message)
                })
        }
    }

    private fun openImagePicker() {
        if (checkAndRequestPermissions(PERMISSIONS, OPEN_IMAGE_PICKER)) {
            openMatisse()
        }
    }

    private fun openMatisse() {
        matisse (
            requestCode = PICK_IMAGE,
            maxSelectable = 1
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {

            // 打开图片选择器时，申请香瓜权限
            OPEN_IMAGE_PICKER -> {
                if (grantResults.permissionGranted()) {
                    openMatisse()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {

            // 拿到图片选择器的结果
            PICK_IMAGE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    vm.profileForm.value = vm.profileForm.value?.copy(
                        displayAvatar = Matisse.obtainPathResult(data).firstOrNull() ?: ""
                    )
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun close() {
        finish()
    }
}
