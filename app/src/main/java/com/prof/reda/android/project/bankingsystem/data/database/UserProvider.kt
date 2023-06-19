package com.prof.reda.android.project.bankingsystem.data.database

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import com.prof.reda.android.project.bankingsystem.data.database.UsersContract.Companion.UserEntry
import com.prof.reda.android.project.bankingsystem.data.database.TransactionsContract.Companion.TransactionsEntry

class UserProvider : ContentProvider() {

    companion object{
        private const val USERS:Int = 200
        private const val USER_ID:Int = 201
        private const val TRANSACTIONS:Int = 210
        private val sUriMatcher = buildUriMatcher()


        private fun buildUriMatcher():UriMatcher{

            val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

            uriMatcher.addURI(UsersContract.CONTENT_AUTHORITY, UsersContract.PATH_USERS, USERS)

            uriMatcher.addURI(UsersContract.CONTENT_AUTHORITY, UsersContract.PATH_USERS + "/#", USER_ID)

            uriMatcher.addURI(TransactionsContract.CONTENT_AUTHORITY, TransactionsContract.PATH, TRANSACTIONS)

            return uriMatcher
        }

    }

    private lateinit var userDbHelper: MyDbHelper

    override fun onCreate(): Boolean {
        userDbHelper = MyDbHelper(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        _selection: String?,
        _selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {

        val database:SQLiteDatabase = userDbHelper.readableDatabase

        var cursor: Cursor?= null

        val match = sUriMatcher.match(uri)

        when(match){
            USERS -> {
                cursor = database.query(UserEntry.TABLE_NAME, projection, _selection, _selectionArgs,
                    null, null, sortOrder)
            }

            USER_ID -> {
                val selection: String?
                val selectionArgs: Array<out String>?

                selection = UserEntry._ID + "=?"
                selectionArgs = arrayOf(ContentUris.parseId(uri).toString())

                cursor = database.query(UserEntry.TABLE_NAME, projection, selection, selectionArgs,
                null, null, sortOrder)
            }

            TRANSACTIONS -> {
                cursor = database.query(TransactionsEntry.TABLE_NAME, projection, _selection, _selectionArgs,
                null, null, sortOrder)
            }

            else -> {
                throw IllegalArgumentException("cannot query unknown URI $uri")
            }
        }

        cursor.setNotificationUri(context?.contentResolver, uri)

        return cursor
    }

    override fun getType(uri: Uri): String {
//        val match = uriMatcher.match(uri)
//        return when(match){
//            USERS -> UserEntry.CONTENT_LIST_TYPE
//
//            USER_ID -> UserEntry.CONTENT_ITEM_TYPE

//            else -> throw IllegalArgumentException("Unknown URI $uri with match $match")
//        }
        throw IllegalArgumentException("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        val match = sUriMatcher.match(uri)
        when(match){
            USERS -> {
                return insertUser(uri, values)
            }

            TRANSACTIONS ->{
                return insertTransaction(uri, values)
            }

            else -> {
                throw IllegalArgumentException("Unknown uri: $uri")
            }
        }
    }

    private fun insertTransaction(uri: Uri, values: ContentValues?): Uri {
        val database = userDbHelper.writableDatabase

        val id = database.insert(TransactionsEntry.TABLE_NAME, null, values)

        if (id.equals(-1)){
            throw SQLException("Failed to insert row into + $uri")
        }

        context?.contentResolver?.notifyChange(uri, null)

        return ContentUris.withAppendedId(uri, id)
    }

    private fun insertUser(uri: Uri, values: ContentValues?): Uri {

        //get writeable database
        val database = userDbHelper.writableDatabase

        val id:Long  = database.insert(UserEntry.TABLE_NAME, null, values)

        if (id.equals(-1)){
            throw SQLException("Failed to insert row into + $uri")
        }

        context?.contentResolver?.notifyChange(uri, null)

        return ContentUris.withAppendedId(uri, id)
    }

    override fun delete(uri: Uri, _selection: String?, _selectionArgs: Array<out String>?): Int {
        val database: SQLiteDatabase = userDbHelper.writableDatabase

        val match = sUriMatcher.match(uri)

        when(match){
            USERS ->{
                return database.delete(UserEntry.TABLE_NAME, _selection, _selectionArgs)
            }

            USER_ID ->{
                val selection:String = UserEntry._ID + "=?"
                val selectionArgs = arrayOf(ContentUris.parseId(uri).toString())

                return database.delete(UserEntry.TABLE_NAME, selection, selectionArgs)
            }

            TRANSACTIONS -> {
                return database.delete(TransactionsEntry.TABLE_NAME, _selection, _selectionArgs)
            }

            else ->{
                throw IllegalArgumentException("Deletion is not supported for $uri")
            }
        }
    }

    override fun update(uri: Uri, values: ContentValues?, _selection: String?,
                        _selectionArgs: Array<out String>?): Int {
        val match = sUriMatcher.match(uri)
        when(match){
            USERS ->{
                return updateUser(uri, values, _selection, _selectionArgs)
            }

            USER_ID ->{
                val selection:String?
                val selectionArgs:Array<out String>?

                selection = UserEntry._ID + "=?"
                selectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                return updateUser(uri, values, selection, selectionArgs)
            }

//            TRANSACTIONS -> {
//                return updataTransaction(uri, values, _selection, _selectionArgs)
//            }

            else ->{
                throw IllegalArgumentException("Update not supported for $uri")
            }
        }
    }

//    private fun updataTransaction(uri: Uri, values: ContentValues?, selection: String?,
//                                  selectionArgs: Array<out String>?): Int{
//        if (values!!.containsKey(TransactionsEntry.co))
//    }
    private fun updateUser(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {

        //if the {@link UserEntry#COLUMN_USERNAME} key is present
        //check that the name is not null
        if (values!!.containsKey(UserEntry.COLUMN_USERNAME)){
            val name = values.getAsString(UserEntry.COLUMN_USERNAME)
            if (name == null){
                throw IllegalArgumentException("require a name")
            }
        }

        //if the {@link UserEntry#COLUMN_EMAIL} key is present
        //check that the email is not null
        if (values.containsKey(UserEntry.COLUMN_EMAIL)){
            val email = values.getAsString(UserEntry.COLUMN_EMAIL)
            if (email == null){
                throw IllegalArgumentException("require an email")
            }
        }

        //if the {@link UserEntry#COLUMN_BALANCE} key is present
        //check that the balance is not null
        if (values.containsKey(UserEntry.COLUMN_BALANCE)){
            val balance = values.getAsString(UserEntry.COLUMN_BALANCE)
            if (balance == null){
                throw IllegalArgumentException("require a balance")
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0){
            return 0
        }

        val database:SQLiteDatabase = userDbHelper.writableDatabase

        return database.update(UserEntry.TABLE_NAME, values, selection, selectionArgs)
    }

}