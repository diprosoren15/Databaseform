package com.example.databaseform

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.databaseform.databinding.ActivityCreateInvoiceBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CreateInvoiceActivity : AppCompatActivity() {

    private lateinit var invoiceBinding: ActivityCreateInvoiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        invoiceBinding = ActivityCreateInvoiceBinding.inflate(layoutInflater)
        setContentView(invoiceBinding.root)

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid

        val email = intent.getStringExtra("email")

        val dealerdbref =
            Firebase.firestore.collection("Dealer_Database").whereEqualTo("email", email)

        dealerdbref.get().addOnSuccessListener { querySnapshot ->

            for (document in querySnapshot) {
                val dealerName = document.getString("full_name")
                setupRetailerSpinner(dealerName.toString())
            }
        }


        invoiceBinding.createInvoiceBtn.setOnClickListener {

            val name = invoiceBinding.editTextDealerName.text.toString()
            val invoiceNo = invoiceBinding.editTextInvoiceNo.text.toString()
            val date = invoiceBinding.editTextDate.text.toString()
            val quantity = invoiceBinding.editTextQuantity.text.toString()
            val amount = invoiceBinding.editTextAmount.text.toString()
            val selectedRetailer = invoiceBinding.spinnerRetailerName.selectedItem?.toString() ?: ""

            addToDatabase(name, date, invoiceNo, quantity, amount, selectedRetailer)

            Toast.makeText(this, "Invoice Created", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupRetailerSpinner(dealerName: String) {
        val spinner = invoiceBinding.spinnerRetailerName
        val dealerListAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mutableListOf())
        dealerListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = dealerListAdapter

        val selectedRetailer = invoiceBinding.spinnerRetailerName.selectedItem?.toString() ?: ""

        dealerListAdapter.clear()

        val retailerUserDb = Firebase.firestore
        retailerUserDb.collection("Retailer_database")
            .whereEqualTo("Dealer", dealerName)
            .get()
            .addOnSuccessListener { query ->
                for (document in query) {
                    val retailerName = document.getString("full_name")
                    if (retailerName != null) {
                        dealerListAdapter.add(retailerName)
                    } else {
                        dealerListAdapter.add("No Retailer Available")
                    }
                }
            }
    }

    private fun addToDatabase(
        name: String?,
        date: String,
        invoiceNo: String,
        quantity: String,
        amount: String,
        selectedRetailer: String
    ) {
        if (name == null) {
            Toast.makeText(this, "Unable to get user name", Toast.LENGTH_SHORT).show()
            return
        }

        val retailerInfo = hashMapOf(
            "dealer_name" to name,
            "date" to date,
            "invoice_number" to invoiceNo,
            "retailer_name" to selectedRetailer,
            "quantity" to quantity,
            "amount" to amount,
            "status" to "pending retailer approval"
        )

        val retailerDb = Firebase.firestore
        retailerDb.collection("Invoice_Information_of_Dealer").add(retailerInfo)
            .addOnSuccessListener { docref ->
                Log.d("TAG", "User added with ${docref.id}")

            }.addOnFailureListener {
            }
    }
}