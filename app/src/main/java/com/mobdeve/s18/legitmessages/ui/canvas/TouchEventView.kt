package com.mobdeve.s18.legitmessages.ui.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView

class TouchEventView(context: Context, attrs: AttributeSet?) :
    AppCompatImageView(context, attrs) {
    private val paint = Paint()
    private val path = Path()
//    val context: Context
    var gestureDetector: GestureDetector

    private inner class GestureListener : SimpleOnGestureListener() {
        // event when double tap occurs
        override fun onDoubleTap(e: MotionEvent): Boolean {
            val x = e.x
            val y = e.y
            path.reset()
            Toast.makeText(context, "Double Tap >> Tapped at: ($x,$y)", Toast.LENGTH_SHORT).show()
            return true
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val eventX = event.x
        val eventY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(eventX, eventY)
                return true
            }
            MotionEvent.ACTION_MOVE -> path.lineTo(eventX, eventY)
            MotionEvent.ACTION_UP -> {
            }
            else -> return false
        }
        gestureDetector.onTouchEvent(event)
        invalidate()
        return true
    }

    init {
        gestureDetector = GestureDetector(context, GestureListener())
//        this.context = context
        paint.isAntiAlias = true
        paint.strokeWidth = 6f
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
    }
}