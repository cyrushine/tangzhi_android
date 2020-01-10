package com.ifanr.tangzhi.ui.updateprofile

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.ext.roundedRect
import com.ifanr.tangzhi.ui.base.BaseItemDecoration
import com.ifanr.tangzhi.ui.base.epoxy.KotlinEpoxyHolder
import com.ifanr.tangzhi.ui.base.model.BaseTyped2Controller
import com.ifanr.tangzhi.ui.base.widget.FlatGridEpoxyRV

/**
 * 背景图片列表
 */
class BannerList: FlatGridEpoxyRV {

    companion object {
        private const val TAG = "BannerList"
    }

    interface Listener {
        fun onItemClick(position: Int) {}
    }

    private val ctl = BannerController()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        layoutManager = FlatGridLayoutManager(context, 3)
        setController(ctl)
        addItemDecoration(BannerItemDecoration(context))
    }

    fun setData(banners: List<String>, selected: String?) {
        Log.d(TAG, "$selected \n ${banners.joinToString()}")
        ctl.setData(banners, selected)
    }

    fun setListener(l: Listener) {
        ctl.listener = l
    }
}

class BannerItemDecoration(ctx: Context): BaseItemDecoration() {

    private val padding = ctx.dp2px(6)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        vh: RecyclerView.ViewHolder,
        vt: Int,
        position: Int
    ) {
        outRect.top = padding
        outRect.bottom = padding
        when (position.rem(3)) {
            0 -> outRect.right = padding
            1 -> {
                outRect.left = padding / 2
                outRect.right = padding / 2
            }
            2 -> outRect.left = padding
        }
    }
}

class BannerController: BaseTyped2Controller<List<String>, String>() {

    var listener: BannerList.Listener = object: BannerList.Listener {}

    override fun buildModels(banners: List<String>?, selected: String?) {
        banners?.forEach {
            bannerItem {
                id(it)
                data(it)
                selected(it == selected)
                onClick { _, _, _, position ->
                    listener.onItemClick(position)
                }
            }
        }
    }
}

@EpoxyModelClass(layout = R.layout.update_profile_banner_item)
abstract class BannerItemModel: EpoxyModelWithHolder<BannerItemModel.Holder>() {

    @EpoxyAttribute
    lateinit var data: String

    @EpoxyAttribute
    var selected: Boolean = false

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onClick: View.OnClickListener

    override fun bind(holder: Holder) {
        Glide.with(holder.image).roundedRect().load(data).into(holder.image)
        listOf(holder.mask, holder.selected).forEach {
            it.visibility = if (selected) View.VISIBLE else View.GONE
        }
        holder.view.setOnClickListener(onClick)
    }

    class Holder: KotlinEpoxyHolder() {
        val image by bind<ImageView>(R.id.image)
        val mask by bind<View>(R.id.mask)
        val selected by bind<View>(R.id.selected)
    }
}