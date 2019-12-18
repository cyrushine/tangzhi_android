package com.ifanr.tangzhi.ui.gallery

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.RequestManager
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.Routes
import com.ifanr.tangzhi.ext.checkAndRequestPermissions
import com.ifanr.tangzhi.ext.permissionGranted
import com.ifanr.tangzhi.ext.toast
import com.ifanr.tangzhi.ui.base.BaseViewModelActivity
import com.ifanr.tangzhi.ui.base.viewModel
import com.ifanr.tangzhi.ui.statusBar
import kotlinx.android.synthetic.main.activity_gallery.*
import javax.inject.Inject
import javax.inject.Provider
import kotlin.math.max

@Route(path = Routes.gallery)
class GalleryActivity : BaseViewModelActivity() {

    companion object {
        private val PERMISSION_TO_DOWNLOAD =
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        private const val REQUEST_DOWNLOAD = 8
    }

    @Autowired(name = Routes.galleryImageList)
    @JvmField
    var items: ArrayList<String> = ArrayList()

    @Autowired(name = Routes.galleryIndex)
    @JvmField
    var index = 0

    @Inject
    lateinit var requestManager: Provider<RequestManager>

    private lateinit var vm: GalleryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        statusBar(whiteText = true)
        index.coerceIn(0, max(items.size - 1, 0))

        vm = viewModel()
        vm.downloadMessage.observe(this, Observer {
            it?.second?.also {
                toast(getString(R.string.gallery_download_fail))
                return@Observer
            }

            it?.first?.also {
                toast(getString(R.string.gallery_download_success, it))
                return@Observer
            }
        })
        vp.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                vm.position.value = position
            }
        })
        vm.images.value = items

        toolbar.close.setOnClickListener { finish() }
        download.setOnClickListener {
            if (checkAndRequestPermissions(PERMISSION_TO_DOWNLOAD, REQUEST_DOWNLOAD)) {
                vm.downloadImageInPosition()
            }
        }

        with(vp) {
            adapter = GalleryAdapter(items, vp)
            indicator.bind(this)
            offscreenPageLimit = 1
            setCurrentItem(index, false)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_DOWNLOAD -> {
                if (grantResults.permissionGranted())
                    vm.downloadImageInPosition()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}
