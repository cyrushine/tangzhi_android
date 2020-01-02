package com.ifanr.tangzhi.ui.widgets

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.getColorCompat
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatterBuilder
import java.util.*

/**
 * 显示日期时间的文本控件：「1分钟前」，「12月4日 13:08」
 */
class DateTextView: AppCompatTextView {

    companion object {
        private const val TAG = "DateTextView"
    }

    private val inYearFormatter by lazy {
        DateTimeFormatter.ofPattern("M月d日 HH:mm") }

    private val commonFormatter by lazy {
        DateTimeFormatter.ofPattern("yyyy年M月d日")
    }

    private val sdf by lazy {
        SimpleDateFormat("M月d日 HH:mm", Locale.getDefault())
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f)
        setTextColor(context.getColorCompat(R.color.base_a8))
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.END
    }

    /**
     * <= 5s，刚刚
     * < 60s，几秒前
     * < 60m，几分钟前
     * < 1d，几小时前
     * < 2d，昨天
     * < 3d，前天
     * 本年度，M月d日 HH:mm
     * 其他，yyyy年M月d日
     */
    fun setDatetime(second: Long) {
        try {
            val now = LocalDateTime.now()
            val target = LocalDateTime.ofEpochSecond(second, 0, ZoneOffset.ofHours(8))
            val duration = Duration.between(target, now)
            val diffDayOfYear = now.dayOfYear - target.dayOfYear
            val text =
                when {
                    duration.seconds <= 5 -> "刚刚"
                    duration.seconds < 60 -> "${duration.seconds}秒前"
                    duration.toMinutes() < 60 -> "${duration.toMinutes()}分钟前"
                    diffDayOfYear == 0 -> "${duration.toHours()}小时前"
                    diffDayOfYear == 1 -> "昨天"
                    diffDayOfYear == 2 -> "前天"
                    now.year == target.year -> target.format(inYearFormatter)
                    else -> target.format(commonFormatter)
                }
            this.text = text
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
            this.text = sdf.format(Date(second * 1000))
        }
    }
}