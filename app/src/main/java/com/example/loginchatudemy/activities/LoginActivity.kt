package com.example.loginchatudemy.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.loginchatudemy.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mGoogleSignInClient: GoogleSignInClient by lazy { getGoogleSignInClient() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
        textViewForgetPassword.setOnClickListener {
            goActivity<ForgotPasswordActivity>()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
        buttonCreateAccount.setOnClickListener {
            goActivity<SignUpActivity>()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        //Validaciones.
        editTextEmailLogIn.validate {
            editTextEmailLogIn.error = if (isValidEmail(it)) null else "El email introducido no es valido"
        }

        editTextPasswordLogIn.validate {
            editTextPasswordLogIn.error = if (isValidPassword(it)) null else "La contraseña debe de contener, una letra miniscula, mayuscula, un número, un caracter especial y tamaño minimo de 8 caracteres"
        }
    }

    private fun getGoogleSignInClient(): GoogleSignInClient{
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(this, gso)
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
