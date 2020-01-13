package com.ifanr.tangzhi.ui.points.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.paging.PagedList
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.model.PointLog
import com.ifanr.tangzhi.ui.base.epoxy.KotlinEpoxyHolder
import com.ifanr.tangzhi.ui.base.model.BasePagedListController
import com.ifanr.tangzhi.ui.base.model.BaseTypedController
import com.ifanr.tangzhi.ui.base.widget.AppEpoxyRV

class PointsRecordList: AppEpoxyRV {

    private val ctl = PointsRecordController()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setController(ctl)
    }

    fun submit(pagedList: PagedList<PointLog>) {
        ctl.submitList(pagedList)
    }

}


class PointsRecordController: BasePagedListController<PointLog>() {

    override fun buildItemModel(currentPosition: Int, item: PointLog?): EpoxyModel<*> {
        val elem = item ?: PointLog()
        return PointsRecordModel_().apply {
            id(elem.id)
            data(elem)
        }
    }
}


@EpoxyModelClass(layout = R.layout.points_record_item)
abstract class PointsRecordModel: EpoxyModelWithHolder<PointsRecordModel.Holder>() {

    @EpoxyAttribute
    lateinit var data: PointLog

    override fun bind(holder: Holder) {
        holder.title.text = data.description
        holder.createdAt.text = data.createdAt
        holder.points.setPoints(data.point)
    }

    class Holder: KotlinEpoxyHolder() {
        val title by bind<TextView>(R.id.title)
        val createdAt by bind<TextView>(R.id.createdAt)
        val points by bind<PointsTextView>(R.id.points)
    }
}