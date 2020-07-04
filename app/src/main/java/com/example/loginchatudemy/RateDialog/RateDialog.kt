package com.example.loginchatudemy.RateDialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.loginchatudemy.R
import com.example.loginchatudemy.models.NewRateEvent
import com.example.loginchatudemy.models.Rate
import com.example.loginchatudemy.toast
import com.example.loginchatudemy.utils.RxBus
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.dialog_rates.view.*
import java.util.*

class RateDialog: DialogFragment(){
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = activity!!.layoutInflater.inflate(R.layout.dialog_rates, null)

        return AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.dialog_title))
            .setView(view)
            .setPositiveButton(getString(R.string.dialog_ok)){ _, _ ->
                val textRate = view.editTextRateFeedback.text.toString()
                val imgURL = FirebaseAuth.getInstance().currentUser!!.photoUrl?.toString() ?: run {""}
                val rate = Rate(textRate, view.ratingBarFeedback.rating, Date(), imgURL)
                RxBus.publish(NewRateEvent(rate))
            }
            .setNegativeButton(getString(R.string.dialog_cancel)){ _, _ ->
                activity!!.toast("Presiona cancelar")
            }
            .create()
    }
}