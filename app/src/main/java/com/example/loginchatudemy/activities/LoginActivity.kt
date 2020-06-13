package com.example.loginchatudemy.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loginchatudemy.*
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    //INICIO DE SESIÓN CON FIREBASE POR MEDIO DE CORREO Y CONTRASEÑA, ADEMAS DE GOOGLE Y SUS VALIDACONES
    //INICIO VARIABLES
    private val RC_SIGN_IN = 423
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    //FIN VARIABLES

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //INICIO LLAMADA DE FUNCION
        googleLogin()
        //FIN LLAMADA DE FUNCION

        //INICIO FUNCION PARA VERIFICAR EL INICIO DE SESIÓN
        buttonSignIn.setOnClickListener {
            val email = editTextEmailLogIn.text.toString()
            val password = editTextPasswordLogIn.text.toString()
            if (isValidEmail(email) && isValidPassword(password)) {
                logInByEmail(email, password)
            } else {
                toast("Asegúrate que los datos ingresados son correctos")
            }
        }
        //FIN FUNCION PARA VERIFICAR EL INICIO DE SESIÓN

        //INICIO EVENTOS ONCLICK
        textViewForgetPassword.setOnClickListener { goActivity<ForgotPasswordActivity>()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
        buttonCreateAccount.setOnClickListener { goActivity<SignUpActivity>()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        //INICIO VALIDACIONES, CORREO Y CONTRASEÑA
        editTextEmailLogIn.validate {
            editTextEmailLogIn.error = if (isValidEmail(it)) null else "El correo introducido no es valido" }

        editTextPasswordLogIn.validate {
            editTextPasswordLogIn.error = if (isValidPassword(it)) null else "La contraseña debe de contener, una letra minúscula, mayúscula, un número, un carácter especial y tamaño mínimo de 8 caracteres" }
        //FIN VALIDACIONES
        //FIN EVENTOS ONCLICK
    }

    //INICIO FUNCION PARA INICIO DE SESIÓN CON GOOGLE
    private fun googleLogin(){
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build())
        //INICIO EVENTO ONCLICK
        buttonLogInGoogle.setOnClickListener {
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setIsSmartLockEnabled(true)
                    .build(), RC_SIGN_IN)
        }
        //FIN EVENTO ONCLIC
    }
    //FIN FUNCION PARA INICIO DE SESIÓN CON GOOGLE

    //INICIO PARA VERIFICAR SI EL CORREO FUE VERIFICADO Y VERIFICAR SI EL USUARIO ESTA ACTIVO
    private fun logInByEmail(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                if (mAuth.currentUser!!.isEmailVerified){
                    toast("La sesión del usuario esta actualmente activa")
                } else {
                    toast("Antes de entrar debes de confirmar tu correo")
                }
            } else {
                toast("Ha ocurrido un error, por favor intenta de nuevo")
            }
        }
    }
    //FIN PARA VERIFICAR SI EL CORREO FUE VERIFICADO Y VERIFICAR SI EL USUARIO ESTA ACTIVO

    //INICIO ONACTIVITYRESULT PARA EL LOGIN CON GOOGLE
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                goActivity<MainActivity> {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            } else {
                toast("Error al iniciar sesion con Google")
            }
        }
    }
    //INICIO ONACTIVITYRESULT PARA EL LOGIN CON GOOGLE
}
