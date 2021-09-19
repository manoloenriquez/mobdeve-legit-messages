package com.mobdeve.s18.legitmessages.ui.canvas

import android.content.ContentValues
import android.content.Intent
import android.graphics.*
import android.media.Image
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.model.ImageMessage
import com.mobdeve.s18.legitmessages.model.User
import java.io.FileNotFoundException


class CanvasActivity : AppCompatActivity(), OnTouchListener, View.OnClickListener {

    private var iv_canvas: ImageView? = null
    private var save_btn: ImageButton? = null
    private var clear_btn: ImageButton? = null
    private var canvas: Canvas? = null
    private var paint: Paint? = null
    private var bitmap: Bitmap? = null
    private var alteredImage: Bitmap? = null
    private var matrix: Matrix? = null
    private val penColor = Color.BLUE
    private var windowWidth = 0f
    private var windowHeight = 0f
    private var downX = 0.0f
    private var downY = 0.0f
    private var upX = 0.0f
    private var upY = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_canvas)
        iv_canvas = findViewById<View>(R.id.iv_canvas) as ImageView
        save_btn = findViewById<View>(R.id.save_btn) as ImageButton
        clear_btn = findViewById<View>(R.id.clear_btn) as ImageButton

        save_btn!!.setOnClickListener(this)
        clear_btn!!.setOnClickListener(this)

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
        paint!!.color = penColor
        paint!!.strokeWidth = 10f
        paint!!.style = Paint.Style.STROKE

        iv_canvas!!.setImageBitmap(bitmap)
        iv_canvas!!.setOnTouchListener(this)
        iv_canvas!!.invalidate()
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = motionEvent.x
                downY = motionEvent.y
            }
            MotionEvent.ACTION_UP -> {
                upX = motionEvent.x
                upY = motionEvent.y
                canvas!!.drawLine(downX, downY, upX, upY, paint!!)
                iv_canvas!!.invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                upX = motionEvent.x
                upY = motionEvent.y
                canvas!!.drawLine(downX, downY, upX, upY, paint!!)
                iv_canvas!!.invalidate()
                downX = upX
                downY = upY
            }
            MotionEvent.ACTION_CANCEL -> {
            }
            else -> {
            }
        }
        return true
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.save_btn -> {
                val imageFileUri = contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    ContentValues()
                )

                val chatId = intent.getStringExtra("chatId")

                try {
                    val imageFileOS = contentResolver
                        .openOutputStream(imageFileUri!!)
                    if (alteredImage != null) {
                        alteredImage!!.compress(Bitmap.CompressFormat.JPEG, 90, imageFileOS)
                        val msg: ImageMessage? =
                            User.currentUser?.uid?.let { ImageMessage(it, imageFileUri) }

                        if (chatId != null) {
                            msg?.send(chatId)
                        }
                    } else {
                        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, imageFileOS)
                        val msg: ImageMessage? = User.currentUser?.uid?.let { ImageMessage(it, imageFileUri) }
                        if (chatId != null) {
                            msg?.send(chatId)
                        }
                    }
                    finish()


                } catch (fnfe: FileNotFoundException) {
                    fnfe.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            R.id.clear_btn -> {
                canvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            }
        }
    }
}