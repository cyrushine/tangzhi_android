package com.ifanr.tangzhi.ui.product.comments.review.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.route.Routes
import com.ifanr.tangzhi.ui.base.epoxy.KotlinEpoxyHolder
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.ext.roundedRect
import com.ifanr.tangzhi.ui.base.model.BaseTypedController
import com.ifanr.tangzhi.ui.base.widget.FlatGridEpoxyRV

/**
 * 评论下的九宫图片
 */
class ImageTable: FlatGridEpoxyRV {

    private val controller = ImageTableController()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        layoutManager = FlatGridLayoutManager(context, 3)
        setController(controller)
        addItemDecoration(ImageTableItemDecoration(context))
        setRecycledViewPool(RecycledViewPool())
    }

    fun setData(images: List<String>) {
        controller.setData(images.take(9))
    }
}


class ImageTableController: BaseTypedController<List<String>>() {

    override fun buildModels(data: List<String>?) {
        data?.forEachIndexed { index, it ->
            imageItem {
                id(it)
                data(it)
                onClick { model, parentView, clickedView, position ->
                    ARouter.getInstance().build(Routes.gallery)
                        .withStringArrayList(Routes.galleryImageList, ArrayList(data))
                        .withInt(Routes.galleryIndex, index)
                        .navigation(clickedView.context)
                }
            }
        }
    }
}


@EpoxyModelClass(layout = R.layout.review_image_item)
abstract class ImageItemModel: EpoxyModelWithHolder<ImageItemModel.Holder>() {

    @EpoxyAttribute
    lateinit var data: String

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onClick: View.OnClickListener

    override fun bind(holder: Holder) {
        Glide.with(holder.image).roundedRect().load(data).into(holder.image)
        holder.image.setOnClickListener { onClick.onClick(it) }
    }

    class Holder: KotlinEpoxyHolder() {
        val image: ImageView by bind(R.id.image)
    }
}

class ImageTableItemDecoration (
    private val ctx: Context
): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(0, 0, 0, 0)

        val vh = parent.getChildViewHolder(view)
        val position = vh.adapterPosition
        when (position.rem(3)) {
            0 -> outRect.right = ctx.dp2px(4)
            1 -> {
                outRect.left = ctx.dp2px(2)
                outRect.right = ctx.dp2px(2)
            }
            2 -> outRect.left = ctx.dp2px(4)
        }
        outRect.bottom = ctx.dp2px(8)
    }
}