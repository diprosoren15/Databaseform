package com.example.databaseform

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.databaseform.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()


        loginBinding.loginAsDealerBtn.setOnClickListener {

            val enteredEmail = loginBinding.editTextEmailOfDealer.text.toString()
            val enteredPassword = loginBinding.editTextPasswordOfDealer.text.toString()



            if (enteredEmail.isNotEmpty() && enteredPassword.isNotEmpty()) {

                auth.signInWithEmailAndPassword(enteredEmail, enteredPassword)
                    .addOnSuccessListener {

                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
            }

        }
    }

    override fun onBackPressed() {
        intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
    }
}