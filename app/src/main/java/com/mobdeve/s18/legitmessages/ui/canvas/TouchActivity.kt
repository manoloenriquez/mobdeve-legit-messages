package com.mobdeve.s18.legitmessages.ui.canvas

import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s18.legitmessages.R
import java.io.FileNotFoundException


class TouchActivity : AppCompatActivity(), View.OnClickListener {
    private var btn_save: Button? = null
    private var btn_open: Button? = null
    private var drawing_pad: TouchEventView? = null
    private var bitmap: Bitmap? = null
    private var canvas: Canvas? = null
    private var paint: Paint? = null
    private var windowWidth = 0f
    private var windowHeight = 0f
    private var alteredImage: Bitmap? = null
    private var matrix: Matrix? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas)
        init()
        val currentDisplay = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(currentDisplay)
        windowHeight = (currentDisplay.heightPixels * 2).toFloat()
        windowWidth = (currentDisplay.widthPixels * 2).toFloat()
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val display2 = windowManager.defaultDisplay
        val outPoint = Point()
        display2.getSize(outPoint)
        var width = 0
        var height = 0
        if (outPoint.y > outPoint.x) {
            height = outPoint.y
            width = outPoint.x
        } else {
            height = outPoint.x
            width = outPoint.y
        }
        bitmap = Bitmap.createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        )
        canvas = Canvas(bitmap as Bitmap)
        paint = Paint()
    }

    private fun init() {
        drawing_pad = findViewById<View>(R.id.drawing_pad) as TouchEventView
        btn_save!!.setOnClickListener(this)
        btn_open!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

    }
}