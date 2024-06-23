package com.example.dietcalculator.androidviews

import android.content.Context
import android.graphics.BlendMode
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.contains
import androidx.core.graphics.toRectF
import com.example.dietcalculator.R

class WeekCalendarView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var mSelectedDay: WeekDays = WeekDays.Monday
    private var labelPosition = 0
    private var mWeekStart: WeekDays = WeekDays.Monday
    private var dayDivision: Map<WeekDays, Int> = mutableMapOf( WeekDays.Tuesday to 2 )
    private var defaultDayDivision: Int = 1
    private var listeners: MutableSet<WeekCalendarListener> = mutableSetOf()
    private var rectanngoleWidth: Float = 1.0f
    private var rectanngoleHeight: Float = 1.0f
    private val spaceRatio = 0.98f
    private val columnRatio = 0.3f
    private val daysNumber : Int = WeekDays.values().size
    private val rectangole: Rect = Rect()

    private val defaultPaint: Paint = Paint().apply {
        this.strokeWidth = 2.0f
    }

    private val rectangolePaint: Paint = Paint().apply {
        this.color = Color.BLACK
        this.style = Paint.Style.STROKE
    }

    private val textPaint: Paint = Paint().apply {
        this.textSize = 20.0f
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.WeekCalendarView,
            0, 0).apply {
            try {
                mSelectedDay =  WeekDays.fromValue(getInteger(R.styleable.WeekCalendarView_selectedDay, WeekDays.Monday.value))
                labelPosition = getInteger(R.styleable.WeekCalendarView_labelPosition, 0)
                mWeekStart = WeekDays.fromValue(getInteger(R.styleable.WeekCalendarView_weekStart, WeekDays.Monday.value))
                defaultDayDivision = getInteger(R.styleable.WeekCalendarView_dayDivision, 1)
            } finally {
                recycle()
            }
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.rectanngoleWidth = w.toFloat()
        this.rectanngoleHeight = h.toFloat()
    }


    fun addListener(l: WeekCalendarListener){
        WeekCalendarListeners.addListener(l)
    }

    fun setDayDivision(dayDivision: Int){
        this.defaultDayDivision = dayDivision
        this.invalidate()
        this.requestLayout()
    }

    fun setWeekStart(weekStart: WeekDays){
        this.mWeekStart = weekStart
        this.invalidate()
        this.requestLayout()
    }

    fun setSelectedDay(selectedDay: WeekDays){
        this.mSelectedDay = selectedDay
        this.invalidate()
        this.requestLayout()
        listeners?.forEach {
            it.onDaySelected(this, selectedDay)
        }
    }

    private fun getDayTouched(x: Float, y: Float): WeekDays?{
        val point = Point(x.toInt(), y.toInt())
        if( !this.rectangole.contains(point) ){
            return null
        }
        return getDayTouched(point, this.rectangole)
    }

    private fun getDayTouched(point: Point, rect: Rect): WeekDays{
        val subrectangoleSize = rect.height()/daysNumber
        return WeekDays.fromValue( (point.y/subrectangoleSize) )
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(this.listeners == null){
            return super.onTouchEvent(event)
        }
        if( event != null ){
            val x = event.x
            val y = event.y
            val day = getDayTouched(x, y)
            day?.let {
                WeekCalendarListeners.notify {
                    l -> l.onDayTouched(this, day)
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val width = this.rectanngoleWidth * spaceRatio
        val height = this.rectanngoleHeight * spaceRatio
        val space = this.width * (1-spaceRatio)
        this.rectangole.set(space.toInt(), space.toInt(), width.toInt(), height.toInt())
        val legendaColumn = width * columnRatio
        textPaint.apply {
            textSize = width * 0.04f
        }
        canvas?.apply {
            drawRect(this@WeekCalendarView.rectangole, rectangolePaint)
            drawCalendarDaysStructure(this, legendaColumn, space, height, width)
            drawLine(legendaColumn, space, legendaColumn, height, defaultPaint)
        }
    }

    private fun drawCalendarDaysStructure(canvas: Canvas, legendaColumn: Float, space: Float, height: Float, width: Float){
        for(i in 0..<daysNumber){
            val currentHeight = height/daysNumber.toFloat() * i
            addDayLines(canvas, i, space, currentHeight, width)
            addTextLabels(canvas, i, height, legendaColumn, daysNumber)
            addDaysDivisionLines(canvas, WeekDays.fromValue(i), legendaColumn, height, width, currentHeight, space)
        }
    }


    private fun addDayLines(canvas: Canvas, i: Int, space: Float, currentHeight: Float, width:Float){
        if(i>0){
            canvas.drawLine(space, currentHeight, width, currentHeight, defaultPaint)
        }
    }

    private fun addDaysDivisionLines(canvas: Canvas, today: WeekDays, legendaColumn: Float, height: Float, width: Float, currentHeight: Float, space: Float){
        val cHeight = if(currentHeight==0f) space else currentHeight
        val currentDayDivision = dayDivision[today] ?: defaultDayDivision
        val subrectangoleSize = height/daysNumber.toFloat()
        if(currentDayDivision>1){
            val columnWidth = width*(1-columnRatio)
            for(i in 1..<currentDayDivision){
                val currentWidth = columnWidth * (i.toFloat()/currentDayDivision.toFloat()) + legendaColumn
                var finalHeight = cHeight + subrectangoleSize
                if(today==WeekDays.Monday){
                    finalHeight -= space
                }
                canvas.drawLine(currentWidth, cHeight, currentWidth, finalHeight, defaultPaint)
            }
        }
    }

    private fun addTextLabels(canvas: Canvas, i: Int, height: Float, legendaColumn:Float, daysNumber: Int){
        val today = WeekDays.fromValue(i)
        val textIndex: Float = i.toFloat() + (1f/2f)
        val textHeight = (height/daysNumber.toFloat()) * textIndex
        val textWidth = legendaColumn*0.15f
        if(mSelectedDay == today){
            canvas.drawTextWithColor(today.toString(), textWidth, textHeight, textPaint, Color.GREEN)
        }
        else{
            canvas.drawText(today.toString(), textWidth, textHeight, textPaint)
        }
    }






}

fun Canvas.drawTextWithColor(s: String, x: Float, y: Float, paint: Paint, color: Int?){
    if(color == null){
        drawText(s, x, y, paint)
        return
    }
    val prevColor = paint.color
    paint.color = color
    drawText(s, x, y, paint)
    paint.color = prevColor
}

