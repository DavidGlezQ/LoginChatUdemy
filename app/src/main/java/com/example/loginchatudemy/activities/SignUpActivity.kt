package com.example.loginchatudemy.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.loginchatudemy.*
import com.google.firebase.auth.FirebaseAuth
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
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        //Verificar que los datos introducidos son correctos.
        buttonSignUpCreate.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val confirmPassword = editTextConfirmPassword.text.toString()
            if (isValidEmail(email) && isValidPassword(password) && isValidConfirmPassword(password, confirmPassword)) {
                signUpByEmail(email, password)
            } else {
                toast("Confirma que todos los datos sean correctos")
            }
        }

        //Validaciones.
        editTextEmail.validate {
            editTextEmail.error = if (isValidEmail(it)) null else "El email introducido no es valido"
        }

        editTextPassword.validate {
            editTextPassword.error = if (isValidPassword(it)) null else "La contraseña debe de contener, una letra miniscula, mayuscula, un número, un caracter especial y tamaño minimo de 8 caracteres"
        }

        editTextConfirmPassword.validate {
            editTextConfirmPassword.error = if (isValidConfirmPassword(editTextPassword.text.toString(), it)) null else "Las contraseñas no son iguales"
        }
    }
        //Metodo para crear al usuario.
        private fun signUpByEmail(email: String, password: String) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //Comprobacion por medio del email.
                        mAuth.currentUser!!.sendEmailVerification().addOnCompleteListener(this){
                            toast("Un email ha sido enviado a tu correo, porfavor confirma para poder entrar")
                            goActivity<LoginActivity> {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        }
                    } else {
                        toast("Un error inesperado ocurrio, porfavor vuelva a intentarlo")
                    }
                }
        }
}

