package com.udacity

import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    private var bgColor = 0
    private var buttonText = ""

    private lateinit var circleAnimator:ValueAnimator
    private var currentSweepAngle =0

    @Volatile
    private var progress: Double = 0.0
    private var angle:Int = 0
    private var valueAnimator: ValueAnimator

    private val updateListener = ValueAnimator.AnimatorUpdateListener {
        progress = (it.animatedValue as Float).toDouble()
        if(progress == 100.0){downloadCompleted()}
        invalidate()    // redraw the screen
        requestLayout() // when rectangular progress dimension changes
    }



    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }

     private fun downloadCompleted(){

         buttonState = ButtonState.Completed
         invalidate()
         requestLayout()
         Log.e("downloadCompleted","completed")
    }

    init {
        isClickable = true
        valueAnimator = AnimatorInflater.loadAnimator(
            context,
            // properties for downloading progress is defined
            R.animator.animator
        ) as ValueAnimator

        valueAnimator.addUpdateListener(updateListener)

        context.withStyledAttributes(attrs,R.styleable.LoadingButton){
            bgColor = getColor(R.styleable.LoadingButton_backGroundColor,0)
            buttonText = this.getString(R.styleable.LoadingButton_buttonText).toString()
            Log.e("init",buttonText)
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        if(buttonState == ButtonState.Completed){ buttonState = ButtonState.Loading}
//        invalidate()
        animateCircle()
        valueAnimator.start()
        return true
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = bgColor
        // draw the button
        canvas?.drawRect(0f,0f,widthSize.toFloat(),heightSize.toFloat(),paint)
        //draw the progress
        if(buttonState == ButtonState.Loading){
            paint.color = resources.getColor(R.color.colorPrimaryDark)
            canvas?.drawRect(
               0f,0f,
                ((widthSize.toFloat() * (progress/100)) ).toFloat(),heightSize.toFloat(),paint
            )

            paint.color = resources.getColor(R.color.colorAccent)

            canvas?.drawArc(
                widthSize - 145f,
                heightSize / 2 - 35f,
                widthSize - 75f,
                heightSize / 2 + 35f,
                90F, // better atheistic than 0
                currentSweepAngle.toFloat(),
                true,
                paint
            )

        }

        //draw text
       paint.color = resources.getColor(R.color.white)
        if(buttonState == ButtonState.Loading){
            buttonText = resources.getString(R.string.button_loading)
        }else{
            buttonText = resources.getString(R.string.download)
        }

        canvas?.drawText(buttonText,widthSize.toFloat()-490,heightSize.toFloat()-60,paint)
        Log.e("LoadingButton", "$heightSize-$widthSize")

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

    private fun animateCircle() {
       circleAnimator = ValueAnimator.ofInt(0, 360).apply {
            duration = 2000
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator ->
                currentSweepAngle = valueAnimator.animatedValue as Int
                invalidate()
            }
        }
        circleAnimator.start()
    }


//    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//      widthSize = w
//      heightSize = h
//    }


}