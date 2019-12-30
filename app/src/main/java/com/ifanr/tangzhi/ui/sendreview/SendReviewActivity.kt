package com.ifanr.tangzhi.ui.sendreview

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.Routes
import com.ifanr.tangzhi.ext.checkAndRequestPermissions
import com.ifanr.tangzhi.ext.getColorCompat
import com.ifanr.tangzhi.ext.permissionGranted
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.sendreview.widget.UploadImageTable
import com.ifanr.tangzhi.ui.statusBar
import com.ifanr.tangzhi.ui.widgets.LoadingDialogFragment
import com.ifanr.tangzhi.ui.widgets.SimpleToolBar
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.filter.Filter
import com.zhihu.matisse.internal.entity.IncapableCause
import com.zhihu.matisse.internal.entity.Item
import kotlinx.android.synthetic.main.activity_send_review.*

@Route(path = Routes.sendReview)
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
            override fun onCancel() { finish() }
            override fun onSend() { finish() }
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
            Matisse.from(this)
                .choose(vm.imageMimeTypes)
                .autoHideToolbarOnSingleTap(true)
                .capture(false)
                .countable(true)
                .imageEngine(GlideEngine())
                .maxSelectable(vm.imageSizeForPick)
                .showPreview(false)
                .theme(R.style.Matisse_TangZhi)
                .addFilter(vm.imageFilter)
                .forResult(START_PICKER)
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


