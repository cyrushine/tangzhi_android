package com.ifanr.tangzhi.ui.product.widgets

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.ifanr.tangzhi.Const
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ext.default
import com.ifanr.tangzhi.ext.inflateInto

/**
 *
 */
class ProductBanner: ConstraintLayout {

    private val maskDrawable: GradientDrawable = GradientDrawable(
        GradientDrawable.Orientation.TOP_BOTTOM,
        intArrayOf(Color.TRANSPARENT, Const.DEFAULT_PRODUCT_THEME))

    private val vp: ViewPager2
    private val adapter: BannerAdapter = BannerAdapter()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        inflateInto(R.layout.product_banner)
        findViewById<ImageView>(R.id.maskIv).setImageDrawable(maskDrawable)
        vp = findViewById(R.id.vp)
        vp.adapter = adapter
        vp.offscreenPageLimit = 2
    }

    fun setThemeColor(@ColorInt color: Int) {
        maskDrawable.colors = intArrayOf(Color.TRANSPARENT, color)
        maskDrawable.invalidateSelf()
    }

    fun setImages(images: List<String>) {
        adapter.items = images
        adapter.notifyDataSetChanged()
    }
}

private class BannerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<String> = emptyList()

    override fun getItemViewType(position: Int): Int =
        R.layout.product_banner_item

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BannerImage(parent)
    }

    override fun getItemCount(): Int =
        items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            R.layout.product_banner_item -> {
                val vh = holder as BannerImage
                val url = items[position]
                Glide.with(vh.image.context).default().load(url).into(vh.image)
                vh.image.setOnClickListener {
                    ARouter.getInstance().build(Routes.gallery)
                        .withStringArrayList(Routes.galleryImageList, ArrayList(items))
                        .withInt(Routes.galleryIndex, position)
                        .navigation(vh.image.context)
                }
            }
        }
    }
}

private class BannerImage(
    parent: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.product_banner_item, parent, false)
) {
    val image: ImageView = itemView as ImageView
}