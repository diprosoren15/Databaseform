package com.example.databaseform

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.databaseform.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var homeBinding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)


        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email.toString()

        homeBinding.createInvoiceBtnTemp.setOnClickListener {

            val intent = Intent(this, CreateInvoiceActivity::class.java)
            intent.putExtra("email", email)
            startActivity(intent)
        }
    }
}