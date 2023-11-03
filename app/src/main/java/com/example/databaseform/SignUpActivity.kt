package com.example.databaseform

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.databaseform.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        loginBinding.dealerSignUpBtn.setOnClickListener {
            startActivity(Intent(this, DealerRegistration::class.java))
            finish()

        }
        loginBinding.retailerSignUpBtn.setOnClickListener {
            startActivity(Intent(this, RetailerRegistration::class.java))
            finish()

        }

        loginBinding.loginAsDealerTextview.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        loginBinding.loginAsRetailerTextview.setOnClickListener {
            startActivity(Intent(this, LoginRetailerActivity::class.java))
            finish()
        }
    }
}