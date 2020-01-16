package com.ifanr.tangzhi.ui.sendreview

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.*
import com.ifanr.tangzhi.route.Extra
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.autoDispose
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.sendreview.widget.UploadImageTable
import com.ifanr.tangzhi.ui.statusBar
import com.ifanr.tangzhi.ui.widgets.SimpleToolBar
import com.ifanr.tangzhi.ui.widgets.dismissLoading
import com.ifanr.tangzhi.ui.widgets.showLoading
import com.uber.autodispose.android.lifecycle.autoDispose
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.engine.impl.GlideEngine
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_send_review.*
import java.util.concurrent.TimeUnit

@Route(path = Routes.sendReview, extras = Extra.signIn)
class SendReviewActivity : BaseViewModelActivity() {

    companion object {
        private val PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        private const val PICK_IMAGE = 8
        private const val START_PICKER = 4
        private const val TAG = "SendReviewActivity"
    }

    @Autowired(name = Routes.sendReviewProductId)
    @JvmField
    var productId: String = ""

    @Autowired(name = Routes.sendReviewProductName)
    @JvmField
    var productName: String = ""

    lateinit var vm: SendReviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_review)
        statusBar(whiteText = false)

        vm = viewModel()
        vm.productName.observe(this, Observer { it?.also {
            toolbar.setTitle(it)
            commentEt.hint = getString(R.string.send_review_hint, it)
        }})
        vm.sendBtnEnable.observe(this, Observer {
            toolbar.sendButtonEnable = it == true
        })
        vm.imagePaths.observe(this, Observer { it?.also {
            images.setImages(it)
        }})
        textRatingBar.onProgressChanged = {
            vm.rating.value = it
        }
        commentEt.addTextChangedListener {
            vm.comment.value = it?.toString()
        }
        toolbar.listener = object: SimpleToolBar.Listener {
            override fun onCancel() {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }

            // 发表评论
            override fun onSend() {
                vm.sendReview()
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.doOnSubscribe { showLoading() }
                    ?.doAfterTerminate { dismissLoading() }
                    ?.autoDispose(this@SendReviewActivity)
                    ?.subscribe({
                        toast(R.string.send_review_success)
                        delay(500) {
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                    }, {
                        Log.e(TAG, it.message, it)
                        toast(it.message ?: "")
                    })
            }
        }
        vm.productId.value = productId
        vm.productName.value = productName

        images.setListener(object: UploadImageTable.Listener {
            override fun onAddClick() { pickImages() }
            override fun onDeleteClick(position: Int) { vm.removeImage(position) }
            override fun onImageClick(position: Int) { previewImage(position) }
        })
    }

    /**
     * 预览图片
     */
    private fun previewImage(position: Int) {
        val images = vm.imagePaths.value
        if (!images.isNullOrEmpty()) {
            ARouter.getInstance().build(Routes.gallery)
                .withStringArrayList(Routes.galleryImageList, ArrayList(images))
                .withInt(Routes.galleryIndex, position)
                .navigation(this)
        }
    }


    private fun pickImages() {
        if (checkAndRequestPermissions(PERMISSIONS, PICK_IMAGE)) {
            startImagePicker()
        }
    }

    private fun startImagePicker() {
        if (vm.imageSizeForPick > 0) {
            matisse (
                requestCode = START_PICKER,
                maxSelectable = vm.imageSizeForPick,
                filter = vm.imageFilter
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            START_PICKER -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    vm.addImage(Matisse.obtainPathResult(data) ?: emptyList(),
                        Matisse.obtainResult(data) ?: emptyList())
                }
            }

            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PICK_IMAGE -> {
                if (grantResults.permissionGranted()) {
                    startImagePicker()
                }
            }

            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

}


