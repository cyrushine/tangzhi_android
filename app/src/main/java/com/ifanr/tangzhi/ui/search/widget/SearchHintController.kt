package com.ifanr.tangzhi.ui.search.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import android.widget.TextView
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.epoxy.KotlinEpoxyHolder
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.ext.getColorCompat
import com.ifanr.tangzhi.model.Product
import com.ifanr.tangzhi.ui.base.BaseItemDecoration
import com.ifanr.tangzhi.ui.base.model.BaseTypedController

class SearchHintController: BaseTypedController<List<Product>>() {

    var onHintClick: (positon: Int) -> Unit = {}

    override fun buildModels(data: List<Product>?) {
        data?.forEach {
            searchHint {
                id(it.id)
                data(it)
                onClick { _, _, _, position ->
                    onHintClick.invoke(position)
                }
            }
        }
    }
}

@EpoxyModelClass(layout = R.layout.search_hint)
abstract class SearchHintModel: EpoxyModelWithHolder<SearchHintModel.Holder>() {

    @EpoxyAttribute
    lateinit var data: Product

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onClick: View.OnClickListener

    override fun bind(holder: Holder) {
        holder.name.text = data.name
        holder.view.setOnClickListener(onClick)
    }

    class Holder: KotlinEpoxyHolder() {
        val name by bind<TextView>(R.id.name)
    }
}

class SearchHintDecoration (
    ctx: Context
): RecyclerView.ItemDecoration() {

    private val paddingLeftRight = ctx.dp2px(20f)

    private val dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = ctx.dp2px(0.5f)
        color = ctx.getColorCompat(R.color.base_f4)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        parent.children.forEach { child ->
            c.drawLine(
                child.left + paddingLeftRight,
                child.bottom.toFloat(),
                child.right - paddingLeftRight,
                child.bottom.toFloat(),
                dividerPaint
            )
        }
    }
}