package com.ifanr.tangzhi.ui.points.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.inflateInto
import com.ifanr.tangzhi.model.PointLog
import com.ifanr.tangzhi.model.PointLog.Companion.TYPE_CONTENT
import com.ifanr.tangzhi.model.PointLog.Companion.TYPE_ITEM_LIST
import com.ifanr.tangzhi.model.PointLog.Companion.TYPE_REPLY
import com.ifanr.tangzhi.model.PointLog.Companion.TYPE_REVOKE
import com.ifanr.tangzhi.model.PointLog.Companion.TYPE_SYSTEM
import com.ifanr.tangzhi.model.PointLog.Companion.TYPE_TESTING
import kotlinx.android.synthetic.main.indexes_view.view.*
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty

/**
 * 积分记录筛选器
 */
class PointFilter: ConstraintLayout {

    private lateinit var filter: TextView

    /**
     * @see TYPE_CONTENT
     * @see TYPE_REPLY
     * @see TYPE_TESTING
     * @see TYPE_ITEM_LIST
     * @see TYPE_SYSTEM
     * @see TYPE_REVOKE
     */
    private var type by Delegates.observable("") { _, old, new ->

        val type = context.getString(when (new) {
            TYPE_CONTENT -> R.string.points_record_type_content
            TYPE_REPLY -> R.string.points_record_type_reply
            TYPE_TESTING -> R.string.points_record_type_testing
            TYPE_ITEM_LIST -> R.string.points_record_type_item_list
            TYPE_SYSTEM -> R.string.points_record_type_system
            TYPE_REVOKE -> R.string.points_record_type_revoke
            else -> R.string.points_record_type_all
        })
        filter.text = "$type ${context.getString(R.string.ic_down_small)}"

        if (old != new) {
            onTypeChanged.invoke(new)
        }
    }

    var onTypeChanged: (String) -> Unit = {}

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        inflateInto(R.layout.point_filter)
        filter = findViewById(R.id.filterTv)
        filter.setOnClickListener { openFilterPopup() }
        type = ""
    }

    private fun openFilterPopup() {
        PopupMenu(context, filter, Gravity.BOTTOM.or(Gravity.START)).apply {
            inflate(R.menu.point_filter)
            setOnMenuItemClickListener {
                type = when (it.itemId) {
                    R.id.point_type_content -> TYPE_CONTENT
                    R.id.point_type_reply -> TYPE_REPLY
                    R.id.point_type_testing -> TYPE_TESTING
                    R.id.point_type_item_list -> TYPE_ITEM_LIST
                    R.id.point_type_system -> TYPE_SYSTEM
                    R.id.point_type_revoke -> TYPE_REVOKE
                    else -> ""
                }
                true
            }
            show()
        }
    }
}