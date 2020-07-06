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
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.dialog_rates.view.*
import java.util.*

class RateDialog: DialogFragment(){

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser
    private fun setUpCurrentUser(){
        currentUser = mAuth.currentUser!!
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setUpCurrentUser()
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_rates, null)

        return AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.dialog_title))
            .setView(view)
            .setPositiveButton(getString(R.string.dialog_ok)){ _, _ ->
                val textRate = view.editTextRateFeedback.text.toString()
                if (textRate.isNotEmpty()){
                    val imgURL = currentUser.photoUrl?.toString() ?: run {""}
                    val rate = Rate(currentUser.uid, textRate, view.ratingBarFeedback.rating, Date(), imgURL)
                    RxBus.publish(NewRateEvent(rate))
                } else {
                    activity!!.toast("El texto no puede estar vacio!")
                }
            }
            .setNegativeButton(getString(R.string.dialog_cancel)){ _, _ ->
            }
            .create()
    }
}