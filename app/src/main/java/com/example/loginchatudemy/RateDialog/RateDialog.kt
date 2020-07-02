package com.example.loginchatudemy.RateDialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.loginchatudemy.R
import com.example.loginchatudemy.toast

class RateDialog: DialogFragment(){
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.dialog_title))
            .setView(R.layout.dialog_rates)
            .setPositiveButton(getString(R.string.dialog_ok)){ dialog, which ->
                activity!!.toast("Presiona ok")
            }
            .setNegativeButton(getString(R.string.dialog_cancel)){ dialog, which ->
                activity!!.toast("Presiona cancelar")
            }
            .create()
    }
}