package com.ifanr.tangzhi.ui.sendreview.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.airbnb.epoxy.*
import com.bumptech.glide.Glide
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.epoxy.KotlinEpoxyHolder
import com.ifanr.tangzhi.ext.roundedRect
import com.ifanr.tangzhi.ui.base.model.BaseEpoxyController
import com.ifanr.tangzhi.ui.base.model.BaseTypedController
import com.ifanr.tangzhi.ui.base.widget.AppEpoxyRV
import com.ifanr.tangzhi.ui.base.widget.FlatGridEpoxyRV
import com.ifanr.tangzhi.util.uuid

/**
 * 上传图片列表
 */
class UploadImageTable: FlatGridEpoxyRV {

    interface Listener {
        fun onAddClick() {}
        fun onDeleteClick(position: Int) {}
        fun onImageClick(position: Int) {}
    }

    private val controller: Controller

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        layoutManager = FlatGridLayoutManager(context, 3)
        controller = Controller()
        setController(controller)
        setImages(emptyList())
    }

    fun setImages(images: List<String>) {
        controller.setData(images)
    }

    fun setListener(l: Listener) {
        controller.listener = l
    }
}

class Controller: BaseTypedController<List<String>>() {

    var listener = object: UploadImageTable.Listener {}

    @AutoModel
    lateinit var add: AddModel_

    override fun buildModels(data: List<String>?) {
        data?.take(9)?.forEach {
            image {
                id(it)
                uri(it)
                onDeleteClick { _, _, _, position ->
                    listener.onDeleteClick(position)
                }
                onImageClick { _, _, _, position ->
                    listener.onImageClick(position)
                }
            }
        }
        if (data.isNullOrEmpty() || data.size < 9) {
            add.onClick(View.OnClickListener { listener.onAddClick() })
            add.addTo(this)
        }
    }

}

@EpoxyModelClass(layout = R.layout.send_review_image_item)
abstract class ImageModel: EpoxyModelWithHolder<ImageModel.Holder>() {

    @EpoxyAttribute
    lateinit var uri: String

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onDeleteClick: View.OnClickListener

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onImageClick: View.OnClickListener

    override fun bind(holder: Holder) {
        Glide.with(holder.image).roundedRect().load(uri).into(holder.image)
        holder.delete.setOnClickListener { onDeleteClick.onClick(it) }
        holder.image.setOnClickListener { onImageClick.onClick(it) }
    }

    class Holder: KotlinEpoxyHolder() {
        val delete by bind<View>(R.id.delete)
        val image by bind<ImageView>(R.id.image)
    }
}

@EpoxyModelClass(layout = R.layout.send_review_image_add)
abstract class AddModel: EpoxyModelWithHolder<AddModel.Holder>() {

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onClick: View.OnClickListener

    override fun bind(holder: Holder) {
        holder.view.setOnClickListener { onClick.onClick(it) }
    }

    class Holder: KotlinEpoxyHolder()
}