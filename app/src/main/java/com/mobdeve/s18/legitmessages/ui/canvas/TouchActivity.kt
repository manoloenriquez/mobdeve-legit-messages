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
//        btn_open = findViewById<View>(R.id.btn_open) as Button
//        btn_save = findViewById<View>(R.id.btn_save) as Button
        drawing_pad = findViewById<View>(R.id.drawing_pad) as TouchEventView
        btn_save!!.setOnClickListener(this)
        btn_open!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
//        when (v.id) {
//            R.id.btn_open -> {
//                Log.d("TouchActivity", "Button Open")
//                val choosePictureIntent = Intent(
//                    Intent.ACTION_PICK,
//                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//                )
//                startActivityForResult(choosePictureIntent, 0)
//            }
//            R.id.btn_save -> Log.d("TouchActivity", "Button Save")
//        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
//        super.onActivityResult(requestCode, resultCode, intent)
//        if (resultCode == RESULT_OK) {
//            val imageFileUri = intent!!.data
//            try {
//                val bmpFactoryOptions = BitmapFactory.Options()
//                bmpFactoryOptions.inJustDecodeBounds = true
//                bitmap = BitmapFactory.decodeStream(
//                    contentResolver
//                        .openInputStream(imageFileUri!!), null, bmpFactoryOptions
//                )
//                val widthRatio =
//                    Math.ceil((bmpFactoryOptions.outWidth / windowWidth).toDouble()).toInt()
//                val heightRatio =
//                    Math.ceil((bmpFactoryOptions.outHeight / windowHeight).toDouble()).toInt()
//                if (heightRatio > widthRatio) {
//                    bmpFactoryOptions.inSampleSize = heightRatio
//                } else {
//                    bmpFactoryOptions.inSampleSize = widthRatio
//                }
//                bmpFactoryOptions.inJustDecodeBounds = false
//                bitmap = BitmapFactory.decodeStream(
//                    contentResolver
//                        .openInputStream(imageFileUri), null, bmpFactoryOptions
//                )
//                alteredImage = Bitmap.createBitmap(bitmap!!.width, bitmap!!.height, bitmap!!.config)
//                canvas = Canvas(alteredImage)
//                matrix = Matrix()
//                canvas!!.drawBitmap(bitmap!!, matrix!!, paint)
//                drawing_pad.setImageBitmap(alteredImage)
//                drawing_pad.invalidate()
//            } catch (fnfe: FileNotFoundException) {
//                Log.e("TouchActivity", fnfe.message!!)
//            } catch (e: Exception) {
//                Log.e("TouchActivity", e.message!!)
//            }
//        }
//    }
}