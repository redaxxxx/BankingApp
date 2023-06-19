package com.prof.reda.android.project.bankingsystem.data.database

import android.net.Uri
import android.provider.BaseColumns

class TransactionsContract {

    companion object{
        const val CONTENT_AUTHORITY:String = "com.prof.reda.android.project.bankingsystem"
        val BASE_CONTENT_URI: Uri = Uri.parse("content://$CONTENT_AUTHORITY")

        const val PATH = "transactions"

        class TransactionsEntry: BaseColumns{
            companion object{
                val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH).build()

                const val TABLE_NAME = "transactions"
                const val _ID = BaseColumns._ID
                const val COLUMN_SENDER_NAME = "sender"
                const val COLUMN_RECEIVER_NAME = "receiver"
                const val COLUMN_AMOUNT = "amount"
                const val COLUMN_STATUS = "status"
            }
        }
    }
}