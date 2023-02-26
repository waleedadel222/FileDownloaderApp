package com.waleed.filedownloaderapp

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

const val circleVerticalPadding = 30f
const val loadingRectScaler = 0.1f
const val loadingArcScaler = 10f

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var centeredTextPositionY = 0f
    private lateinit var loadingRect: RectF
    private val valueAnimator = ValueAnimator.ofInt(0, 360).setDuration(3000)
    private var progress = 0

    var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { _, _, new ->
        progress = 0

        when (new) {
            ButtonState.Loading -> {
                valueAnimator.start()
            }
            ButtonState.Completed -> {
                valueAnimator.cancel()
                progress = 0
            }
            else -> {
                valueAnimator.cancel()
                progress = 0
            }
        }
        invalidate()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val loadingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.teal_700)
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
        color = Color.BLACK
    }

    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.YELLOW
    }

    init {
        isClickable = true
        valueAnimator.addUpdateListener {
            progress = it.animatedValue as Int
            invalidate()
        }
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            paint.color = getColor(R.styleable.LoadingButton_defaultColor, 0)
            loadingPaint.color = getColor(R.styleable.LoadingButton_downloadColor, 0)
            textPaint.color = getColor(R.styleable.LoadingButton_textColor, 0)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        loadingRect = RectF(0f, 0f, widthSize.toFloat(), heightSize.toFloat())
        centeredTextPositionY = (h / 2 - (textPaint.descent() + textPaint.ascent()) / 2)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)

        loadingRect.right = progress * loadingRectScaler * widthSize.toFloat()
        canvas.drawRect(loadingRect, loadingPaint)

        canvas.drawText(
            if (buttonState is ButtonState.Loading)
                context.getString(R.string.downloading)
            else context.getString(R.string.begin_download),
            (widthSize / 2).toFloat(),
            centeredTextPositionY,
            textPaint
        )

        canvas.drawArc(
            widthSize - 120f,
            circleVerticalPadding,
            widthSize - 30f,
            heightSize.toFloat() - circleVerticalPadding,
            0f,
            progress.toFloat() * loadingArcScaler,
            true,
            arcPaint
        )


    }

    override fun performClick(): Boolean {
        buttonState = ButtonState.Loading
        invalidate()
        super.performClick()
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}