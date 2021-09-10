package com.mobdeve.s18.legitmessages.ui.message

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mobdeve.s18.legitmessages.R

class EditDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let{
            val alertBuilder = AlertDialog.Builder(it)

            alertBuilder.setTitle("Select an option")
//            alertBuilder.setItems(R.array.popup_array, DialogInterface.OnClickListener{dialog, index ->
//                Toast.makeText(it, index.toString(), Toast.LENGTH_SHORT).show()
//            })
            alertBuilder.create()
        } ?: throw IllegalStateException("Exception !! Activity is null !!")
    }

}