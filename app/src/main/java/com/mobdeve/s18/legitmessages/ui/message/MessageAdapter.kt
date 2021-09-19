package com.mobdeve.s18.legitmessages.ui.message

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s18.legitmessages.R
import com.mobdeve.s18.legitmessages.model.Database
import com.mobdeve.s18.legitmessages.model.ImageMessage
import com.mobdeve.s18.legitmessages.model.Message
import com.mobdeve.s18.legitmessages.model.User
import com.squareup.picasso.Picasso
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class MessageAdapter(private val list: ArrayList<Message>, context: Context):
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>(), TextToSpeech.OnInitListener {

    final var MSG_TYPE_LEFT = 0
    final var MSG_TYPE_RIGHT = 1
    final var IMG_TYPE_LEFT = 2
    final var IMG_TYPE_RIGHT = 1
    lateinit var tts: TextToSpeech

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            Log.i("TTS", "success")
            val result = tts.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.i("TTS", "Language not supported")
            }
        } else {
            Log.i("TTS", "error")
        }
    }

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val message: LinearLayout = view.findViewById(R.id.message_layout)
        val messageBox: TextView = view.findViewById(R.id.show_message)
        val imageBox: ImageView = view.findViewById(R.id.show_image)
        val timeStamp: TextView = view.findViewById(R.id.time_stamp)

        val db = Database()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdapter.MessageViewHolder {
        return if(viewType == MSG_TYPE_RIGHT) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_item_right, parent, false)
            MessageAdapter.MessageViewHolder(view)
        }
        else{
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_item_left, parent, false)
            MessageAdapter.MessageViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {

        if(list[position] is ImageMessage){
//            holder.imageBox.setImageBitmap(getBitmapFromURL((list[position] as ImageMessage).uri))
            Picasso.get().load((list[position] as ImageMessage).uri).into(holder.imageBox)
            holder.imageBox.visibility = View.VISIBLE
            holder.messageBox.visibility = View.GONE
        }
        else{
            holder.messageBox.text = list[position].message
        }

        holder.timeStamp.text = list[position].timeStampString()
        val db = Database()

        if(list[position].sender == User.currentUser?.uid.toString()){
            holder.message.setOnLongClickListener { v: View ->
                val popup = PopupMenu(v.context, holder.message)
                popup.menuInflater.inflate(R.menu.chat_menu, popup.menu)


                popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    when(item.itemId) {
                        R.id.edit_message -> {
                            val intent = Intent(v.context, EditMessageActivity::class.java)
                            intent.putExtra("message", holder.messageBox.text )
                            intent.putExtra("chatId", list[position].chatId)
                            intent.putExtra("id", list[position].id)
                            v.context.startActivity(intent)
                        }

                        R.id.delete_message -> {
                            db.deleteMessage(list[position].chatId, list[position].id)
                        }

                        R.id.text_to_speech -> {
                            tts.speak(list[position].message, TextToSpeech.QUEUE_FLUSH, null, "")
                        }
                    }
                    true
                })
                popup.show()
                true
            }
        } else {
            holder.message.setOnLongClickListener { v ->
                Toast.makeText(v.context, "You can't modify this message.", Toast.LENGTH_SHORT).show()
                true
            }
        }
    }

    override fun getItemViewType(position: Int): Int{
        return if(list[position].sender == User.currentUser?.uid.toString()) {
            MSG_TYPE_RIGHT
        } else
            MSG_TYPE_LEFT
    }

    override fun getItemCount() = list.size

    fun playMessage(message: String) {
        tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    fun getBitmapFromURL(src: Uri): Bitmap? {
        return try {
            Log.e("src", src.toString())
            val url = URL("https://firebasestorage.googleapis.com/v0/b/mobdeve-project.appspot.com/o/lQf81P0aX6KLmHEDmSHC%2F38?alt=media&token=dc7ea032-9937-4ebb-a2ea-c1e9481aa25e")
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            val myBitmap = BitmapFactory.decodeStream(input)
            Log.e("Bitmap", "returned")
            myBitmap
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("Exception", e.toString())
            null
        }
    }

}

