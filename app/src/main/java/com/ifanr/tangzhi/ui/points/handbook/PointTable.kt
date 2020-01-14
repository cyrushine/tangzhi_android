package com.ifanr.tangzhi.ui.points.handbook

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.ext.getColorCompat
import com.ifanr.tangzhi.ui.base.BaseItemDecoration
import com.ifanr.tangzhi.ui.base.epoxy.KotlinEpoxyHolder
import com.ifanr.tangzhi.ui.base.model.BaseTypedController
import com.ifanr.tangzhi.ui.base.widget.FlatVerticalEpoxyRV

/**
 * 积分说明表格
 */
class PointTable: FlatVerticalEpoxyRV {

    companion object {
        private val RULES = listOf(
            "发表内容" to "+10",
            "参与回复" to "+1",
            "参与众测" to "+10",
            "获得精选" to "+30",
            "获得置顶" to "+50",
            "获得众测资格" to "+100",
            "创建清单" to "+20",
            "其他奖励" to "+ ∞"
        )
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setController(PointTableCtl().apply { setData(RULES) })
        addItemDecoration(PointTableDecoration(context))
    }
}

class PointTableCtl: BaseTypedController<List<Pair<String, String>>>() {

    @AutoModel
    lateinit var header: TableHeaderModel_

    override fun buildModels(data: List<Pair<String, String>>?) {
        add(header)
        data?.forEach {
            item {
                id(it.first + it.second)
                data(it)
            }
        }
    }
}


@EpoxyModelClass(layout = R.layout.points_table_header)
abstract class TableHeaderModel: EpoxyModelWithHolder<TableHeaderModel.Holder>() {
    class Holder: KotlinEpoxyHolder()
}

@EpoxyModelClass(layout = R.layout.points_table_item)
abstract class ItemModel: EpoxyModelWithHolder<ItemModel.Holder>() {

    @EpoxyAttribute
    lateinit var data: Pair<String, String>

    override fun bind(holder: Holder) {
        holder.key.text = data.first
        holder.value.text = data.second
    }

    class Holder: KotlinEpoxyHolder() {
        val key by bind<TextView>(R.id.key)
        val value by bind<TextView>(R.id.value)
    }
}

class PointTableDecoration (ctx: Context): BaseItemDecoration() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ctx.getColorCompat(R.color.base_e8)
        style = Paint.Style.STROKE
        strokeWidth = ctx.dp2px(0.5f)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        parent.children.forEach { child ->
            val position = parent.getChildViewHolder(child).adapterPosition
            val left = child.left + paint.strokeWidth
            val top = child.top + paint.strokeWidth
            val right = child.right - paint.strokeWidth
            val bottom = child.bottom - paint.strokeWidth
            val lines = floatArrayOf(
                left, bottom, left, top,            // left
                left, top, right, top,              // top
                right, top, right, bottom,          // right
                right, bottom, left, bottom)        // bottom
            if (position != 0) {
                lines[4] = 0f
                lines[5] = 0f
                lines[6] = 0f
                lines[7] = 0f
            }
            c.drawLines(lines, paint)
        }
    }
}