package com.example.databaseform

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ShowInvoicesRetailerActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemListAdapter: ItemListAdapter
    private val invoiceList = mutableListOf<invoice>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_invoices_retailer)

        val retailerName = intent.getStringExtra("name")
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager= LinearLayoutManager(this)

        val invoiceRef = Firebase.firestore
            .collection("Invoice_Information_of_Dealer")
            .whereEqualTo("retailer_name", retailerName)
        invoiceRef.get().addOnSuccessListener { querySnapshot ->

            Toast.makeText(this, "Document Found", Toast.LENGTH_SHORT).show()


            for (document in querySnapshot) {
                val invoice = invoice(
                    document.getString("dealer_name") ?: "",
                    document.getString("date") ?: "",
                    document.getString("invoice_number") ?: "",
                    document.getString("quantity") ?: "",
                    document.getString("amount") ?: "",
                    document.getString("status") ?: ""
                )
                invoiceList.add(invoice)
            }
            itemListAdapter = ItemListAdapter(invoiceList, this)
            itemListAdapter.setOnClickingItem(object : ItemListAdapter.onItemClickListener {
                override fun onItemClicked(position: Int) {
                    val intent =
                        Intent(applicationContext, RetailerInvoiceDetailsActivity::class.java)
                    val invoice = invoiceList[position]
                    //name,date, invoiceNo ,quantity, amount, selectedRetailer
                    intent.putExtra("retailername", retailerName)
                    intent.putExtra("dealername", invoice.dealer_name)
                    intent.putExtra("date", invoice.date)
                    intent.putExtra("invoiceNo", invoice.invoice_no)
                    intent.putExtra("quantity", invoice.quantity)
                    intent.putExtra("amount", invoice.amount)
                    intent.putExtra("status", invoice.status)
                    startActivity(intent)
                    finish()

                }
            })
            itemListAdapter.notifyDataSetChanged()
            recyclerView.adapter = itemListAdapter


        }
    }
}