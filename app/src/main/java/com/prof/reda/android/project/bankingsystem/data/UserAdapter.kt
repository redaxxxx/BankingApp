package com.prof.reda.android.project.bankingsystem.data

import android.content.Context
import android.database.Cursor
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prof.reda.android.project.bankingsystem.UserViewHolder
import com.prof.reda.android.project.bankingsystem.data.database.UsersContract.Companion.UserEntry
import com.prof.reda.android.project.bankingsystem.databinding.UserItemLayoutBinding

class UserAdapter(private val mContext:Context, private val onItemClickListener: OnItemClickListenerDatabase): RecyclerView.Adapter<UserViewHolder>() {

    private var cursor: Cursor? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(UserItemLayoutBinding.inflate(LayoutInflater.from(mContext),
        parent, false))
    }

    override fun getItemCount(): Int {
        if (cursor == null){
            return 0
        }
        Log.d("Banking System", "cursor size is " + cursor!!.count.toString())
        return cursor!!.count
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val idIndex = cursor!!.getColumnIndex(UserEntry._ID)
        val nameColumnIndex:Int = cursor!!.getColumnIndex(UserEntry.COLUMN_USERNAME)
        val emailColumnIndex:Int = cursor!!.getColumnIndex(UserEntry.COLUMN_EMAIL)
        val balanceColumnIndex:Int = cursor!!.getColumnIndex(UserEntry.COLUMN_BALANCE)

        if (!cursor!!.moveToPosition(position)){
            return
        }
//        cursor!!.moveToPosition(position)

        val id = cursor!!.getInt(idIndex)
        holder.itemView.tag = id
        holder.binding.usernameTv.text = cursor!!.getString(nameColumnIndex)
        holder.binding.emailTv.text = cursor!!.getString(emailColumnIndex)
        holder.binding.balanceTv.text = cursor!!.getString(balanceColumnIndex)

        holder.itemView.setOnClickListener {
            onItemClickListener.onClick(id)
        }
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
    }

    interface OnItemClickListenerDatabase {
        fun onClick(id: Int)
    }

}