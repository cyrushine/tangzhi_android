package com.ifanr.tangzhi.ui.index.home

import android.content.Context
import android.graphics.Rect
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.TextAppearanceSpan
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.*
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.Routes
import com.ifanr.tangzhi.epoxy.KotlinEpoxyHolder
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.ext.roundedRect
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.ui.base.model.BaseTypedController
import com.ifanr.tangzhi.ui.base.model.ListEndSlogonModel_
import com.ifanr.tangzhi.ui.base.widget.AppEpoxyRV
import com.ifanr.tangzhi.ui.widgets.AvatarGroup
import com.ifanr.tangzhi.ui.widgets.RatingBar
import com.ifanr.tangzhi.ui.widgets.ScoreTextView
import java.text.NumberFormat

class ProductList: AppEpoxyRV {

    private val controller = Controller()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        setController(controller)
        setBackgroundResource(R.color.base_f8)
        addItemDecoration(object: ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: State
            ) {
                outRect.set(0, 0, 0, 0)
                val vh = parent.getChildViewHolder(view)
                val vt = vh.itemViewType
                val ctx = parent.context

                if (vt == R.layout.home_product_item) {
                    outRect.bottom = ctx.dp2px(12)
                    outRect.left = ctx.dp2px(20)
                    outRect.right = ctx.dp2px(20)
                }
            }
        })
    }

    fun setData(data: List<Product>) {
        controller.setData(data)
    }
}

class Controller: BaseTypedController<List<Product>>() {

    private val reviewFormat by lazy {
        NumberFormat.getNumberInstance().apply {
            isGroupingUsed = true
        }
    }

    @AutoModel
    lateinit var header: HomeHeaderModel_
    @AutoModel
    lateinit var end: ListEndSlogonModel_

    override fun buildModels(data: List<Product>?) {
        if (!data.isNullOrEmpty()) {
            add(header)
            data.forEach {
                product {
                    id(it.id)
                    data(it)
                    format(reviewFormat)
                }
            }
            add(end)
        }
    }
}

@EpoxyModelClass(layout = R.layout.home_product_item)
abstract class ProductModel: EpoxyModelWithHolder<ProductModel.Holder>() {

    @EpoxyAttribute
    lateinit var data: Product

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var format: NumberFormat

    override fun bind(holder: Holder) {
        Glide.with(holder.cover).roundedRect().load(data.coverImage).into(holder.cover)
        holder.name.text = data.name
        holder.ratingBar.setProgress(data.rating)
        holder.score.setScore(data.rating)
        holder.avatars.setAvatars(data.participant)
        holder.reviewCount.text = SpannableStringBuilder()
            .append(format.format(data.reviewCount),
                TextAppearanceSpan(holder.ctx, R.style.AppTextAppearance_HomeProductReview),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            .append(" ")
            .append(holder.ctx.getText(R.string.home_product_review))
        holder.brief.text = data.brief

        holder.view.setOnClickListener {
            ARouter.getInstance().build(Routes.product)
                .withString(Routes.productId, data.id)
                .navigation(holder.ctx)
        }
    }

    class Holder: KotlinEpoxyHolder() {
        val cover by bind<ImageView>(R.id.cover)
        val name by bind<TextView>(R.id.name)
        val ratingBar by bind<RatingBar>(R.id.ratingBar)
        val score by bind<ScoreTextView>(R.id.score)
        val avatars by bind<AvatarGroup>(R.id.avatars)
        val reviewCount by bind<TextView>(R.id.reviewCount)
        val brief by bind<TextView>(R.id.brief)
    }
}

@EpoxyModelClass(layout = R.layout.home_header)
abstract class HomeHeaderModel: EpoxyModelWithHolder<HomeHeaderModel.Holder>() {

    override fun bind(holder: Holder) {
        holder.searchBar.setOnClickListener {
            ARouter.getInstance().build(Routes.search).navigation(it.context)
        }
    }

    class Holder: KotlinEpoxyHolder() {
        val searchBar by bind<View>(R.id.searchBar)
    }
}