package com.prof.reda.android.project.bankingsystem.data

import android.database.Cursor
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.prof.reda.android.project.bankingsystem.data.database.TransactionsContract
import com.prof.reda.android.project.bankingsystem.data.database.TransactionsContract.Companion.TransactionsEntry
import com.prof.reda.android.project.bankingsystem.data.database.UsersContract
import com.prof.reda.android.project.bankingsystem.databinding.RecentTransactionItemsBinding

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    private var cursor: Cursor? = null

    class TransactionViewHolder(val binding: RecentTransactionItemsBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder(RecentTransactionItemsBinding.inflate(LayoutInflater.from(parent.context),
        parent, false))
    }

    override fun getItemCount(): Int {
        if (cursor == null){
            return 0
        }

        Log.v("Banking System", "cursor size: " + cursor!!.count)

        return cursor!!.count
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val idIndex = cursor!!.getColumnIndex(TransactionsEntry._ID)
        val senderNameIndex = cursor!!.getColumnIndex(TransactionsEntry.COLUMN_SENDER_NAME)
        val receiverNameIndex = cursor!!.getColumnIndex(TransactionsEntry.COLUMN_RECEIVER_NAME)
        val amountIndex = cursor!!.getColumnIndex(TransactionsEntry.COLUMN_AMOUNT)
        val statusIndex = cursor!!.getColumnIndex(TransactionsEntry.COLUMN_STATUS)

        if (!cursor!!.moveToPosition(position)){
            return
        }

        val id = cursor!!.getInt(idIndex)
        holder.itemView.tag = id
        holder.binding.senderNameTv.text = cursor!!.getString(senderNameIndex)
        holder.binding.receiverNameTv.text = cursor!!.getString(receiverNameIndex)
        holder.binding.transferAmountTv.text = cursor!!.getString(amountIndex)
        holder.binding.statusTv.text = cursor!!.getString(statusIndex)
    }

    fun swapCursor(c: Cursor?): Cursor? {
        if (cursor == c){
            return null
        }

        val tempCursor = cursor

        if (c != null) {
            this.cursor = c
            notifyDataSetChanged()
        }

        return tempCursor
    }}