package com.example.loginchatudemy.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loginchatudemy.R
import com.example.loginchatudemy.goActivity
import com.example.loginchatudemy.toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class LoginActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (mAuth.currentUser == null) {
            toast("nope")
        } else {
            toast("yep")
            mAuth.signOut()
        }

        //Evento para verificar el inicio de sesion del usuario.
        buttonSignIn.setOnClickListener {
            val email = editTextEmailLogIn.text.toString()
            val password = editTextPasswordLogIn.text.toString()
            if (isValidEmailAndPassword(email, password)) {
                logInByEmail(email, password)
            }
        }

        //Eventos para el olvido de contraseña y crear cuenta.
        textViewForgetPassword.setOnClickListener { goActivity<ForgotPasswordActivity>() }
        buttonCreateAccount.setOnClickListener { goActivity<SignUpActivity>() }
    }

    //Metodo para verificar si la sesion del usuario esta activa o de lo contrario mandar un error.
    private fun logInByEmail(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                toast("La sesion del usuario esta actualmente activa.")
            } else {
                toast("Ha ocurrido un error, porfavor intenta de nuevo.")
            }
        }
    }

    //Metodo para validar el email y la contraeña del usuario.
    private fun isValidEmailAndPassword(email: String, password: String): Boolean {
        return !email.isNullOrEmpty() && !password.isNullOrEmpty()
    }
}
