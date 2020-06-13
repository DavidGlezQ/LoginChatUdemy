package com.example.loginchatudemy.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loginchatudemy.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {
    //RECUPERAR CONTRASEÑA POR MEDIO DEL CORREO
    //INICIO VARIABLES
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    //FIN VARIABLES

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        //INICIO EVENTOS ONCLICK, VALIDACIONES PARA EL CORREO
        //INICIO VALIDACION PARA EL CORREO
        editTextEmailForgot.validate {
            editTextEmailForgot.error = if (isValidEmail(it)) null else "El correo no es valido"
        }
        //FIN VALIDACION PARA EL CORREO

        //INICIO REGRESAR AL LOGIN
        buttonGoLoginForgot.setOnClickListener{
            goActivity<LoginActivity>()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        //FIN REGRESAR AL LOGIN

        //INICIO BOTON PARA RECUPERAR CONTRASEÑA
        buttonForgot.setOnClickListener {
            val email = editTextEmailForgot.text.toString()
            if (isValidEmail(email)) {
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this){
                    toast("Un email ha sido enviado a tu correo")
                    goActivity<LoginActivity>(){
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            } else {
                toast("Por favor asegúrate de que el email es correcto")
            }
        }
        //FIN BOTON PARA RECUPERAR CONTRASEÑA
    }
    //FIN EVENTOS ONCLICK, VALIDACIONES PARA EL CORREO
}
