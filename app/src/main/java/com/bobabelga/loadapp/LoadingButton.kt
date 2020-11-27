package com.bobabelga.loadapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import com.bobabelga.R
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var animatedColorBtn = 0
    private var animatedTxt = ""
    private var btnName = resources.getString(R.string.button_name)
    private val paintBtn = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ResourcesCompat.getColor(resources, R.color.colorPrimary, null)
    }
    private val paintBtnAnim = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null)
    }
    private val paintCircle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ResourcesCompat.getColor(resources, R.color.colorPrimary, null)
    }
    private val paintTxt = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        color = ResourcesCompat.getColor(resources, R.color.white, null)
    }

    var changedWidth = 0f
    var animator = ObjectAnimator()

    private var buttonState: ButtonState by Delegates
        .observable(ButtonState.Completed) { p, old, new ->
            invalidate()
        }

    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            animatedColorBtn = getColor(R.styleable.LoadingButton_animatedColorBtn, 0)
            animatedTxt = getString(R.styleable.LoadingButton_animatedTxt).toString()
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        animator.setFloatValues(0f, widthSize.toFloat())
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                isClickable = false
                Log.d(TAG, "onAnimationStart: Start")
            }

            override fun onAnimationEnd(animation: Animator?) {
                isClickable = true
                Log.d(TAG, "onAnimationStart: End")
            }
        })
        animator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator?) {
                changedWidth = animation?.animatedValue as Float
                invalidate()
                Log.d(TAG, "onAnimationListenerAdapter: ${animation.animatedValue}")
            }

        })
        when (buttonState) {
            ButtonState.Clicked -> {
                animator.start()
            }
            ButtonState.Completed -> {
               animator.cancel()
            }
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val bounds = Rect()
        paintTxt.getTextBounds(btnName, 0, btnName.length, bounds)

        when (buttonState) {
            ButtonState.Clicked -> {
                paintBtnAnim.color =
                    ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null)
                paintCircle.color = ResourcesCompat.getColor(resources, R.color.colorAccent, null)
                btnName = animatedTxt
                Log.d(TAG, "ButtonState: Clicked")
            }
            ButtonState.Completed -> {
                btnName = resources.getString(R.string.button_name)
                paintBtnAnim.color = paintBtn.color
                paintCircle.color = paintBtn.color
                Log.d(TAG, "ButtonState: Completed")
            }
        }

        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBtn)
        canvas?.drawRect(0f, 0f, changedWidth, height.toFloat(), paintBtnAnim)
        canvas?.drawText(
            btnName,
            (widthSize / 2).toFloat(),
            (heightSize / 2 + bounds.height() / 2).toFloat(),
            paintTxt
        )
        canvas?.translate(
            (widthSize / 2 + bounds.width() / 2).toFloat(),
            (heightSize / 4 + bounds.height() / 2).toFloat()
        )
        canvas?.drawArc(
            0f,
            0f,
            bounds.height().toFloat(),
            bounds.height().toFloat(),
            0f,
            (360 * changedWidth) / widthSize,
            false,
            paintCircle
        )

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
    fun setBtnState(btnstate:ButtonState){
        buttonState = btnstate
    }
}