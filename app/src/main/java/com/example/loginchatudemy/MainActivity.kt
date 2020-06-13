package com.example.loginchatudemy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loginchatudemy.activities.LoginActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    //INICIO VARIABLES
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    //FIN VARIABLES

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        SignOut()

    }

    private fun SignOut(){
        buttonSignOut.setOnClickListener {
            buttonSignOut.isEnabled = false
            AuthUI.getInstance().signOut(this).addOnSuccessListener {
                goActivity<LoginActivity>()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                toast("Hasta pronto")
                finish()
            }.addOnFailureListener{
                buttonSignOut.isEnabled = true
                toast("Error")
            }
        }
    }
}
