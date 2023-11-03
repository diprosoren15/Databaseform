package com.example.databaseform

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemListAdapter(
    private var itemList: List<invoice>,
    private var context: Context
) : RecyclerView.Adapter<ItemListAdapter.ItemListViewHolder>() {

    private lateinit var myListener: onItemClickListener


     inner class ItemListViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        var dealerName: TextView
        var invoiceNo: TextView
        var date: TextView
        var quantity: TextView
        var amount: TextView
        var status: TextView

        init {
            dealerName = itemView.findViewById(R.id.textViewDealerName)
            invoiceNo = itemView.findViewById(R.id.textViewInvNumber)
            date = itemView.findViewById(R.id.textViewDate)
            quantity = itemView.findViewById(R.id.textViewQuantity)
            amount = itemView.findViewById(R.id.textViewAmount)
            status = itemView.findViewById(R.id.textViewStatus)

            itemView.setOnClickListener {

                listener.onItemClicked(adapterPosition)

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.each_invoice_item, parent, false)
        return ItemListViewHolder(view, myListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        val eachItem = itemList[position]
        holder.dealerName.text = eachItem.dealer_name
        holder.invoiceNo.text = eachItem.invoice_no
        holder.date.text = eachItem.date
        holder.quantity.text = eachItem.quantity
        holder.amount.text = eachItem.amount
        holder.status.text = eachItem.status


    }

    interface onItemClickListener {
        fun onItemClicked(position: Int)
    }

    fun setOnClickingItem(listener: onItemClickListener) {
        myListener = listener
    }
}