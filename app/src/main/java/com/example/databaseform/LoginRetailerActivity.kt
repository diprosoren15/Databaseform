package com.example.databaseform

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.databaseform.databinding.ActivityLoginRetailerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginRetailerActivity : AppCompatActivity() {

    private lateinit var retailerLoginBinding: ActivityLoginRetailerBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retailerLoginBinding = ActivityLoginRetailerBinding.inflate(layoutInflater)
        setContentView(retailerLoginBinding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        retailerLoginBinding.loginAsRetailerBtn.setOnClickListener {

            val email = retailerLoginBinding.editTextEmailOfRetailer.text.toString()
            val password = retailerLoginBinding.editTextPasswordOfRetailer.text.toString()

            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, RetailerHomeActivity::class.java)
                intent.putExtra("email", email)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "User Not registered", Toast.LENGTH_SHORT).show()
            }

        }
    }
}