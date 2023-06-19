package com.prof.reda.android.project.bankingsystem.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.prof.reda.android.project.bankingsystem.data.database.UsersContract.Companion.UserEntry
import com.prof.reda.android.project.bankingsystem.data.database.TransactionsContract.Companion.TransactionsEntry

class MyDbHelper(val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME: String = "users.db"
        private const val DATABASE_VERSION: Int = 1

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val SQL_CREATE_USERS_TABLE: String = "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UserEntry.COLUMN_USERNAME + " TEXT NOT NULL, " +
                UserEntry.COLUMN_EMAIL + " TEXT NOT NULL, " +
                UserEntry.COLUMN_BALANCE + " INTEGER NOT NULL, " +
                UserEntry.COLUMN_PHONE_NUMBER + " TEXT NOT NULL, " +
                UserEntry.COLUMN_CARD_NUMBER + " TEXT NOT NULL);"


        val SQL_CREATE_TARNSACTIONS_TABLE: String = "CREATE TABLE " + TransactionsEntry.TABLE_NAME + " (" +
                TransactionsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TransactionsEntry.COLUMN_SENDER_NAME + " TEXT NOT NULL, " +
                TransactionsEntry.COLUMN_RECEIVER_NAME + " TEXT NOT NULL, " +
                TransactionsEntry.COLUMN_AMOUNT + " INTEGER NOT NULL, " +
                TransactionsEntry.COLUMN_STATUS + " TEXT NOT NULL);"

        db?.execSQL(SQL_CREATE_USERS_TABLE)
        db?.execSQL(SQL_CREATE_TARNSACTIONS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME)
        db?.execSQL("DROP TABLE IF EXISTS " + TransactionsEntry.TABLE_NAME)
        onCreate(db)
    }

}