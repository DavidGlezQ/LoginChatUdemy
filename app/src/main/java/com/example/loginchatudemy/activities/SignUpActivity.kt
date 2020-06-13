package com.example.loginchatudemy.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.loginchatudemy.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {
    //CREAR USUARIOS POR MEDIOS DE FIREBASE JUNTO CON VALIDACIONES
    //INICIO VARIABLES
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    //FIN VARIABLES

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //INICIO EVENTO PARA REGRESAR AL LOGIN
        buttonGoLogin.setOnClickListener {
            goActivity<LoginActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        //FIN EVENTO PARA REGRESAR AL LOGIN

        //INICIO PARA VERIFICAR QUE LOS DATOS INTRODUCIDOS SON CORRECTOS
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
        //FIN PARA VERIFICAR QUE LOS DATOS INTRODUCIDOS SON CORRECTOS

        //INICIO VALIDACIONES PARA LOS EDITTEXT
        editTextEmail.validate {
            editTextEmail.error = if (isValidEmail(it)) null else "El email introducido no es valido"
        }

        editTextPassword.validate {
            editTextPassword.error = if (isValidPassword(it)) null else "La contraseña debe de contener, una letra miniscula, mayuscula, un número, un caracter especial y tamaño minimo de 8 caracteres"
        }

        editTextConfirmPassword.validate {
            editTextConfirmPassword.error = if (isValidConfirmPassword(editTextPassword.text.toString(), it)) null else "Las contraseñas no son iguales"
        }
        //INICIO VALIDACIONES PARA LOS EDITTEXT
    }

        //INICIO FUNCION PARA CREAR EL USUARIO
        private fun signUpByEmail(email: String, password: String) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //INICIO ENVIÓ CORREO
                        mAuth.currentUser!!.sendEmailVerification().addOnCompleteListener(this){
                            toast("Un email ha sido enviado a tu correo, por favor confirma para poder entrar")
                            goActivity<LoginActivity> {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        }
                        //FIN ENVIÓ CORREO
                    } else {
                        toast("Un error inesperado ocurrió, por favor vuelva a intentarlo")
                    }
                }
        }
    //FIN FUNCION PARA CREAR EL USUARIO
}

