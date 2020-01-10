package com.ifanr.tangzhi.ui.search.widget

import android.graphics.Rect
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.dp2px
import com.ifanr.tangzhi.ui.base.BaseItemDecoration
import com.ifanr.tangzhi.ui.base.epoxy.KotlinEpoxyHolder
import com.ifanr.tangzhi.ui.base.model.BaseTyped2Controller
import com.ifanr.tangzhi.ui.widgets.SearchKeyFlowLayout

/**
 * 搜索的主页
 */
class SearchIndexController (
    private val listener: Listener
): BaseTyped2Controller<List<String>, List<String>>() {

    interface Listener {
        fun onClearHistoryClick() {}
        fun onHistoryClick(position: Int) {}
        fun onHottestClick(position: Int) {}
    }

    // 搜索历史
    @AutoModel
    lateinit var historyModel: SearchIndexModel_

    // 热门推荐
    @AutoModel
    lateinit var hottestModel: SearchIndexModel_

    override fun buildModels(history: List<String>?, hottest: List<String>?) {

        if (!history.isNullOrEmpty()) {
            with(historyModel) {
                titleRes(R.string.search_category_history)
                data(history)
                deleteVisiable(true)
                onDeleteClick { _, _, _, _ ->
                    listener.onClearHistoryClick()
                }
                onItemClick { listener.onHistoryClick(it) }
            }
            add(historyModel)
        }

        if (!hottest.isNullOrEmpty()) {
            with(hottestModel) {
                titleRes(R.string.search_category_hot)
                data(hottest)
                deleteVisiable(false)
                onDeleteClick { _ -> }
                onItemClick { listener.onHottestClick(it) }
            }
            add(hottestModel)
        }
    }
}

@EpoxyModelClass(layout = R.layout.search_index_item)
abstract class SearchIndexModel: EpoxyModelWithHolder<SearchIndexModel.Holder>() {

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onDeleteClick: View.OnClickListener

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onItemClick: (Int) -> Unit

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var deleteVisiable = false

    @EpoxyAttribute
    @StringRes
    var titleRes: Int = 0

    @EpoxyAttribute
    lateinit var data: List<String>

    override fun bind(holder: Holder) {
        holder.title.setText(titleRes)
        holder.delete.setOnClickListener(onDeleteClick)
        holder.delete.visibility = if (deleteVisiable) View.VISIBLE else View.INVISIBLE
        holder.list.setData(data)
        holder.list.onItemClick = { onItemClick.invoke(it) }
    }

    class Holder: KotlinEpoxyHolder() {
        val title by bind<TextView>(R.id.title)
        val delete by bind<View>(R.id.delete)
        val list by bind<SearchKeyFlowLayout>(R.id.list)
    }
}

class SearchIndexDecoration: BaseItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        vh: RecyclerView.ViewHolder,
        vt: Int,
        position: Int
    ) {
        outRect.set(0, 0, 0, 0)
        outRect.top = view.context.dp2px(if (position == 0) 10 else 15)
    }
}