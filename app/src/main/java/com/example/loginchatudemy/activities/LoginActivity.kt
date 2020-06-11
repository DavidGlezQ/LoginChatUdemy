package com.example.loginchatudemy.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loginchatudemy.*
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 423
    }
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        googleLogin()
        //Evento para verificar el inicio de sesion del usuario.
        buttonSignIn.setOnClickListener {
            val email = editTextEmailLogIn.text.toString()
            val password = editTextPasswordLogIn.text.toString()
            if (isValidEmail(email) && isValidPassword(password)) {
                logInByEmail(email, password)
            } else {
                toast("Asegurate que los datos ingresados son correctos")
            }
        }

        //Eventos para el olvido de contraseña y crear cuenta.
        textViewForgetPassword.setOnClickListener { goActivity<ForgotPasswordActivity>()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
        buttonCreateAccount.setOnClickListener { goActivity<SignUpActivity>()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        //Validaciones.
        editTextEmailLogIn.validate {
            editTextEmailLogIn.error = if (isValidEmail(it)) null else "El email introducido no es valido" }

        editTextPasswordLogIn.validate {
            editTextPasswordLogIn.error = if (isValidPassword(it)) null else "La contraseña debe de contener, una letra miniscula, mayuscula, un número, un caracter especial y tamaño minimo de 8 caracteres" }
    }

    fun googleLogin(){
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build())

        buttonLogInGoogle.setOnClickListener {
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(), RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                toast("Inicio con Google")
            } else {
                toast("Error con Google")
            }
        }

    }

    //Metodo para verificar si la sesion del usuario esta activa o de lo contrario mandar un error.
    private fun logInByEmail(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                if (mAuth.currentUser!!.isEmailVerified){
                    toast("La sesion del usuario esta actualmente activa")
                } else {
                    toast("Antes de entrar debes de confirmar tu correo")
                }
            } else {
                toast("Ha ocurrido un error, porfavor intenta de nuevo")
            }
        }
    }
}
