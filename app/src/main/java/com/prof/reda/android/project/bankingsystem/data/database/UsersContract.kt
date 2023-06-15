package com.prof.reda.android.project.bankingsystem.data.database

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns

class UsersContract {

    companion object{
        const val CONTENT_AUTHORITY:String = "com.prof.reda.android.project.bankingsystem"
        val BASE_CONTENT_URI: Uri = Uri.parse("content://$CONTENT_AUTHORITY")

        const val PATH_USERS:String = "users"

        class UserEntry: BaseColumns{
            companion object {

            val CONTENT_URI:Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USERS).build()

            val CONTENT_LIST_TYPE:String =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USERS

            val CONTENT_ITEM_TYPE:String =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USERS

                const val TABLE_NAME: String = "users"

                const val _ID:String = BaseColumns._ID

                const val COLUMN_USERNAME:String = "name"

                const val COLUMN_EMAIL: String = "email"

                const val COLUMN_BALANCE: String = "balance"

                const val COLUMN_PHONE_NUMBER = "phone"

                const val COLUMN_CARD_NUMBER = "card"

            }

        }
    }



}