package com.ifanr.tangzhi.ui.widgets

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.text.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.dp2px
import java.lang.reflect.Constructor
import kotlin.properties.Delegates

private const val TAG = "SpringTextView"

class SpringTextView: View {

    companion object {
        private const val MAX_LINES = 4
    }

    private val textPaint: TextPaint = TextPaint()
    private val expandBtn: ExpandButton = ExpandButton(context)
    private lateinit var textLayout: StaticLayout
    private var requestLayout = true
    private var lineSpacingMultiplier = 1.0f

    var text: String by Delegates.observable("") { _, old, new ->
        if (old != new) {
            requestLayout = true
            requestLayout()
        }
    }

    private var expand = false
    private var textLineCount = 0

    constructor(context: Context?) : super(context) { init(null) }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init(attrs) }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init(attrs) }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) { init(attrs) }

    private fun init(attrs: AttributeSet?) {
        var textColor = Color.WHITE
        var textSize = context.dp2px(14)

        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.SpringTextView)
            textColor = ta.getColor(
                R.styleable.SpringTextView_springTextViewColor, textColor)
            textSize = ta.getDimensionPixelSize(
                R.styleable.SpringTextView_springTextViewSize, textSize)
            lineSpacingMultiplier = ta.getFloat(
                R.styleable.SpringTextView_springTextViewLineSpacingMultiplier, lineSpacingMultiplier)
            ta.recycle()
        }

        textPaint.also {
            it.color = textColor
            it.textSize = textSize.toFloat()
        }

        expandBtn.also {
            it.textSize = textSize.toFloat()
        }

        setOnClickListener {
            if (!expand) {
                expand = true
                requestLayout = true
                requestLayout()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        var height = measuredHeight

        if (requestLayout || width != measuredWidth) {
            textLayout = StaticLayoutCompat.build(
                source = text,
                paint = textPaint,
                width = width,
                maxLines = Int.MAX_VALUE,
                spacingMult = lineSpacingMultiplier
            ).second
            height = textLayout.height
            textLineCount = textLayout.lineCount

            if (!expand && textLineCount > MAX_LINES) {
                textLayout = StaticLayoutCompat.build(
                    source = text,
                    paint = textPaint,
                    width = width,
                    maxLines = MAX_LINES,
                    spacingMult = lineSpacingMultiplier
                ).second
                height = (textLayout.height + expandBtn.height).toInt()
            }
            requestLayout = false
        }
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        textLayout.draw(canvas)
        if (!expand && textLineCount > MAX_LINES) {
            expandBtn.draw(measuredWidth, measuredHeight, canvas)
        }
    }
}


private class ExpandButton (ctx: Context): TextPaint() {

    private val paddingRight = ctx.dp2px(8)
    private val paddingTop = ctx.dp2px(3)

    private val text: String =
        ctx.getString(R.string.product_description_expand)

    val height: Float
        get() = fontMetrics.bottom - fontMetrics.top + paddingTop

    val width: Float
        get() = measureText(text) + paddingRight

    init {
        color = Color.WHITE
    }

    fun draw(canvasWidth: Int, canvasHeight: Int, canvas: Canvas) {
        canvas.drawText(
            text,
            canvasWidth - width,
            canvasHeight - fontMetrics.bottom,
            this)
    }
}


private object StaticLayoutCompat {

    private var doBuildFail: Boolean? = null
    private var cachedConstructor: Constructor<StaticLayout>? = null

    /**
     * @return setting maxLines fail if first == false
     */
    fun build (
        source: CharSequence,
        paint: TextPaint,
        width: Int,
        maxLines: Int = Int.MAX_VALUE,
        spacingMult: Float = 1.0f
    ): Pair<Boolean, StaticLayout> {

        var success: Boolean
        var layout: StaticLayout
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                layout = doBuild23(source, paint, width, maxLines, spacingMult)
                success = true

            }
            doBuildFail == true -> {
                layout = doBuildFallback(source, paint, width, spacingMult)
                success = false

            }
            else -> {
                try {
                    layout = doBuild(source, paint, width, maxLines, spacingMult)
                    success = true
                    doBuildFail = false
                } catch (e: Exception) {
                    Log.e(TAG, e.message, e)
                    layout = doBuildFallback(source, paint, width, spacingMult)
                    success = false
                    doBuildFail = true
                }
            }
        }
        return success to layout
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun doBuild23 (
        source: CharSequence,
        paint: TextPaint,
        width: Int,
        maxLines: Int,
        spacingMult: Float
    ): StaticLayout {
        return StaticLayout.Builder
            .obtain(source, 0, source.length, paint, width)
            .setBreakStrategy(Layout.BREAK_STRATEGY_SIMPLE)
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setEllipsize(TextUtils.TruncateAt.END)
            .setTextDirection(TextDirectionHeuristics.FIRSTSTRONG_LTR)
            .setIncludePad(true)
            .setMaxLines(maxLines)
            .setLineSpacing(0f, spacingMult)
            .build()
    }

    private fun doBuildFallback (
        source: CharSequence,
        paint: TextPaint,
        width: Int,
        spacingMult: Float
    ): StaticLayout {
        return StaticLayout(
            source,
            paint,
            width,
            Layout.Alignment.ALIGN_NORMAL,
            spacingMult,
            0f,
            true)
    }

    @Throws(Exception::class)
    private fun doBuild (
        source: CharSequence,
        paint: TextPaint,
        width: Int,
        maxLines: Int,
        spacingMult: Float
    ): StaticLayout {
        if (cachedConstructor == null) {
            val constructor = StaticLayout::class.java.getDeclaredConstructor(
                CharSequence::class.java,             // CharSequence source
                Int::class.java,                      // int bufstart
                Int::class.java,                      // int bufend
                TextPaint::class.java,                // TextPaint paint
                Int::class.java,                      // int outerwidth
                Layout.Alignment::class.java,         // Alignment align
                TextDirectionHeuristic::class.java,   // TextDirectionHeuristic textDir
                Float::class.java,                    // float spacingmult
                Float::class.java,                    // float spacingadd
                Boolean::class.java,                  // boolean includepad
                TextUtils.TruncateAt::class.java,     // TextUtils.TruncateAt ellipsize
                Int::class.java,                      // int ellipsizedWidth
                Int::class.java                       // int maxLines
            )
            if (!constructor.isAccessible)
                constructor.isAccessible = true
            cachedConstructor = constructor
        }
        return cachedConstructor!!.newInstance(
            source,
            0,
            source.length,
            paint,
            width,
            Layout.Alignment.ALIGN_NORMAL,
            TextDirectionHeuristics.FIRSTSTRONG_LTR,
            spacingMult,
            0f,
            true,
            TextUtils.TruncateAt.END,
            width,
            maxLines
        )
    }
}


