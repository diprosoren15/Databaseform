package com.example.databaseform

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.databaseform.databinding.ActivityRetailerInvoiceDetailsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RetailerInvoiceDetailsActivity : AppCompatActivity() {

    private lateinit var retailerInvoice:ActivityRetailerInvoiceDetailsBinding
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retailerInvoice = ActivityRetailerInvoiceDetailsBinding.inflate(layoutInflater)
        setContentView(retailerInvoice.root)

        val dealerName = intent.getStringExtra("dealername")
        val date = intent.getStringExtra("date")
        val invoiceNo = intent.getStringExtra("invoiceNo")
        val quantity = intent.getStringExtra("quantity")
        val amount = intent.getStringExtra("amount")
        val status = intent.getStringExtra("status")

        retailerInvoice.textViewInvoiceDealer.text = dealerName
        retailerInvoice.textViewInvoiceDate.text = date
        retailerInvoice.textViewInvoiceNumber.text = invoiceNo
        retailerInvoice.textViewInvoiceQuantity.text = quantity
        retailerInvoice.textViewInvoiceAmount.text = amount
        retailerInvoice.textViewInvoiceStatus.text = status

        retailerInvoice.buttonAcceptInvoice.setOnClickListener {
            updateInvoiceStatusOnAccept(invoiceNo.toString())
            retailerInvoice.buttonAcceptInvoice.visibility= View.GONE
            retailerInvoice.buttonRejectInvoice.visibility= View.GONE
            finish()
        }

        retailerInvoice.buttonRejectInvoice.setOnClickListener {
            updateInvoiceStatusOnReject(invoiceNo.toString())
            retailerInvoice.buttonAcceptInvoice.visibility= View.GONE
            retailerInvoice.buttonRejectInvoice.visibility= View.GONE
            finish()
        }
    }

    private fun updateInvoiceStatusOnAccept(invoiceNo: String) {
        // Get the reference to the invoice document.
        val invoiceRef = Firebase.firestore
            .collection("Invoice_Information_of_Dealer")
            .whereEqualTo("invoice_number", invoiceNo)
        invoiceRef.get().addOnSuccessListener { querySnapshot ->

            Toast.makeText(this, "Document Found", Toast.LENGTH_SHORT).show()

            for (document in querySnapshot) {
                val currentStatus = document.getString("status")
                if (currentStatus == "pending retailer approval") {
                    document.reference.update("status", "pending Krednote approval")
                } else {
                    retailerInvoice.buttonAcceptInvoice.visibility = View.GONE
                    retailerInvoice.buttonRejectInvoice.visibility = View.GONE
                }
            }
        }
    }

    private fun updateInvoiceStatusOnReject(invoiceNo: String) {
        // Get the reference to the invoice document.
        val invoiceRef = Firebase.firestore
            .collection("Invoice_Information_of_Dealer")
            .whereEqualTo("invoice_number", invoiceNo)
        invoiceRef.get().addOnSuccessListener { querySnapshot ->

            Toast.makeText(this, "Document Found", Toast.LENGTH_SHORT).show()

            for (document in querySnapshot) {
                val currentStatus = document.getString("status")
                if (currentStatus == "pending retailer approval") {
                    document.reference.update("status", "rejected by retailer")
                }else {
                    retailerInvoice.buttonAcceptInvoice.visibility = View.GONE
                    retailerInvoice.buttonRejectInvoice.visibility = View.GONE
                }
            }
        }
    }
}