package com.mobdeve.s18.legitmessages.ui.image_picker

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.databinding.ActivityImagePickerBinding
import com.mobdeve.s18.legitmessages.model.ImageMessage
import com.mobdeve.s18.legitmessages.model.User
import java.lang.Exception

class ImagePickerActivity : AppCompatActivity() {

    private lateinit var binding : ActivityImagePickerBinding

    var msg: ImageMessage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityImagePickerBinding.inflate(layoutInflater)

        binding.camera.setOnClickListener {
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 1)
        }

        binding.imageSelect.setOnClickListener {
            var intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivity(intent)
            startActivityForResult(intent, 2)
        }

        binding.done.setOnClickListener {
            msg?.send("lQf81P0aX6KLmHEDmSHC")
            finish()
        }

        setContentView(binding.root)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1){
            var bmp: Bitmap = data?.extras?.get("data") as Bitmap
            binding.imageView.setImageBitmap(bmp)

            val imageUri = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ContentValues()
            )

            try {
                val imageFileOut = imageUri?.let { contentResolver.openOutputStream(it) }
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, imageFileOut)

                msg = imageUri?.let { User.currentUser?.uid?.let { it1 ->
                    ImageMessage(
                        it1, it)
                } }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }else if(requestCode == 2 && resultCode == RESULT_OK){
            binding.imageView.setImageURI(data?.data)

            msg = User.currentUser?.uid?.let { data?.data?.let { it1 ->
                ImageMessage(it,
                    it1
                )
            } }
        }

    }

    override fun onStart() {
        super.onStart()

        if(binding.imageView.drawable == null)
            binding.done.visibility = View.GONE
        else
            binding.done.visibility = View.VISIBLE
    }
}