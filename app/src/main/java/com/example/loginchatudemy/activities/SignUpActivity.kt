package com.example.loginchatudemy.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.loginchatudemy.R
import com.example.loginchatudemy.goActivity
import com.example.loginchatudemy.toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //Regresar al login.
        buttonGoLogin.setOnClickListener {
            goActivity<LoginActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }

        //Verificar que los datos introducidos son correctos.
        buttonSignUpCreate.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            if (isValidEmailAndPassword(email, password)) {
                signUpByEmail(email, password)
                toast("Los datos introducidos han sido guardados con exito.")
            } else {
                toast("Ha ocurrido un error inesperado, vuelva a intentar.")
            }
        }

        //Validar si el usuario esta o no logeado
        val currentUser = mAuth.currentUser
        if (currentUser == null){
            toast("User is NOT logged in")
        } else {
            toast("User IS logged in")
        }
    }

    //Metodo para crear al usuario.
    private fun signUpByEmail(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    toast("Un email ha sido enviado a tu correo, porfavor confirma antes de entrar.")
                } else {
                    toast("Un error inesperado ocurrio, porfavor vuelva a intentarlo.")
                }
            }
    }

    //Metodo para validar si los datos son correctos
    private fun isValidEmailAndPassword(email: String, password: String): Boolean {
        return !email.isNullOrEmpty() &&
                !password.isNullOrEmpty() &&
                password == editTextConfirmPassword.text.toString()
    }
}