package com.example.databaseform

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RetailerHomeActivity : AppCompatActivity() {

    private lateinit var retailerName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retailer_home)

        val email = intent.getStringExtra("email")

//        val show = findViewById<TextView>(R.id.textViewemailShow)
//        show.text = email


        retailerName = "null"
        val retailerdb = Firebase.firestore
            .collection("Retailer_database")
            .whereEqualTo("email_address",email)
        retailerdb.get().addOnSuccessListener { querySnapshot ->

            for (doc in querySnapshot){
                retailerName = doc.getString("full_name").toString()

            }
            val show = findViewById<TextView>(R.id.textViewemailShow)
            show.text = retailerName

            val showInvoicesBtn = findViewById<Button>(R.id.buttonshowinvoices)

            showInvoicesBtn.setOnClickListener {

                val intent = Intent(this, ShowInvoicesRetailerActivity::class.java)
                intent.putExtra("name", retailerName)
                startActivity(intent)

            }
        }
    }
}
