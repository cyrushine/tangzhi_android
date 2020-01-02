package com.ifanr.tangzhi.ui.search.widget

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.epoxy.KotlinEpoxyHolder
import com.ifanr.tangzhi.ui.base.model.BaseTyped2Controller
import com.ifanr.tangzhi.ui.widgets.SearchKeyFlowLayout

/**
 * first 搜索历史
 * second 热门推荐
 */
class SearchCategoryController: BaseTyped2Controller<List<String>, List<String>>() {

    @AutoModel
    lateinit var history: SearchCategoryModel_

    @AutoModel
    lateinit var hot: SearchCategoryModel_

    override fun buildModels(first: List<String>?, second: List<String>?) {
        if (!second.isNullOrEmpty()) {
            with(hot) {
                titleRes(R.string.search_category_hot)
                data(second)
                deleteVisiable(false)
                onDeleteClick { _ -> }
                onItemClick {  }
            }
            add(hot)
        }
    }
}

@EpoxyModelClass(layout = R.layout.search_category)
abstract class SearchCategoryModel: EpoxyModelWithHolder<SearchCategoryModel.Holder>() {

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onDeleteClick: View.OnClickListener

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onItemClick: (Int) -> Unit

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var deleteVisiable = false

    @EpoxyAttribute
    @StringRes var titleRes: Int = 0

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