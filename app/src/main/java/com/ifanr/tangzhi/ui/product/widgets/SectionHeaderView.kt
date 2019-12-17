package com.ifanr.tangzhi.ui.product.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.dp2px
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject

/**
 * 每个章节的 header
 */
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT, baseModelClass = SectionHeaderModel::class)
class SectionHeaderView: ConstraintLayout {

    private val titleTv: TextView
    private val countTv: TextView
    val hotspot: View

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        LayoutInflater.from(context).inflate(R.layout.section_header_view, this, true)
        setPadding(0, context.dp2px(40), 0, context.dp2px(10))
        titleTv = findViewById(R.id.titleTv)
        countTv = findViewById(R.id.totalTv)
        hotspot = findViewById(R.id.hotspot)
    }

    @TextProp
    fun setTitle(title: CharSequence) {
        titleTv.text = title
    }

    @TextProp
    fun setCount(count: CharSequence) {
        countTv.text = count
    }

}

abstract class SectionHeaderModel<T: View>: EpoxyModel<T>() {

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var listener: View.OnClickListener

    override fun bind(view: T) {
        (view as? SectionHeaderView)?.hotspot?.setOnClickListener(listener)
    }
}