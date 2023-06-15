package com.prof.reda.android.project.bankingsystem.ui.activities

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.loader.app.LoaderManager.LoaderCallbacks
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.prof.reda.android.project.bankingsystem.*
import com.prof.reda.android.project.bankingsystem.data.UserAdapter
import com.prof.reda.android.project.bankingsystem.data.database.UserDbHelper
import com.prof.reda.android.project.bankingsystem.data.database.UsersContract
import com.prof.reda.android.project.bankingsystem.data.database.UsersContract.Companion.UserEntry
import com.prof.reda.android.project.bankingsystem.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity(), LoaderCallbacks<Cursor>, UserAdapter.OnItemClickListenerDatabase {

    private lateinit var binding:ActivityHomeBinding
    val USER_LOADER_ID: Int = 0
    private lateinit var mAdapter: UserAdapter
    private lateinit var nameList: List<String>
    private lateinit var emailList: List<String>
    private lateinit var balanceList: List<Int>
    private lateinit var phoneNumber:List<String>
    private lateinit var cardNumber:List<String>

    private lateinit var dbHelper: UserDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        dbHelper = UserDbHelper(this)
        mAdapter = UserAdapter(this, this)

        nameList = arrayListOf(
            "Reda Nabil", "Mohamed Nabil", "Mohamed Ezzt", "Malek Ghali", "Ahmed Ibrahim",
            "Mazen Ghali", "Nabil Salem", "Malak Mohamed", "Hussen Gaber", "Reham Nabil"
        )

        emailList = arrayListOf(
            "redanabil@gmail.com",
            "mohamed@gmail.com",
            "mohamedezzt@gmail.com",
            "malek@gmail.com",
            "ahmed@gmail.com",
            "mazen@gmail.com",
            "nabil@gmail.com",
            "malak@gmail.com",
            "hussen@gmail.com",
            "reham@gmail.com"
        )

        balanceList = arrayListOf(5000, 3000, 24522, 7800, 10500, 2000, 47555, 3232, 4500, 9000)

        phoneNumber = arrayListOf("01125572678", "01026451186", "01256548469", "01115654877", "01156897974",
        "01265646978", "01566333265", "01148489650", "01145530580", "01023545454")

        cardNumber = arrayListOf("5555 5555 5555 4444", "4012 8888 8888 1881", "4111 1111 1111 1111",
        "6011 0009 9013 9424", "374245455400126", "378282246310005", "6250941006528599", " 5431 1111 1111 1111 ",
        "3711 1111 1111 114", "3711 1111 1111 114")

//        val db = dbHelper.writableDatabase
//        val values = ContentValues()
//        for (i in 0..9){
//            values.put(UserEntry.COLUMN_USERNAME, nameList[i])
//            values.put(UserEntry.COLUMN_EMAIL, emailList[i])
//            values.put(UserEntry.COLUMN_BALANCE, balanceList[i])
//            values.put(UserEntry.COLUMN_PHONE_NUMBER, phoneNumber[i])
//            values.put(UserEntry.COLUMN_CARD_NUMBER, cardNumber[i])
//            db.insert(UserEntry.TABLE_NAME, null, values)
//        }

        binding.usersRV.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.VERTICAL, false)
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }

        supportLoaderManager.initLoader(USER_LOADER_ID, null, this)
    }

    private fun insertUsers() {
        val contentValue = ContentValues()
        for (i in 0..9){
            contentValue.put(UserEntry.COLUMN_USERNAME, nameList[i])
            contentValue.put(UserEntry.COLUMN_EMAIL, emailList[i])
            contentValue.put(UserEntry.COLUMN_BALANCE, balanceList[i])
            contentValue.put(UserEntry.COLUMN_PHONE_NUMBER, phoneNumber[i])
            contentValue.put(UserEntry.COLUMN_CARD_NUMBER, cardNumber[i])

            val uri = contentResolver.insert(UsersContract.Companion.UserEntry.CONTENT_URI, contentValue)
        }
    }

    private fun deleteAllUsers(){
        val rowsDeleted = contentResolver.delete(UsersContract.Companion.UserEntry.CONTENT_URI, null, null)
        Log.v("Banking System", "$rowsDeleted rows deleted from user database")
    }

    override fun onClick(id: Int) {
        intent = Intent(this, UserDetailsActivity::class.java)
        intent.putExtra(KEY_ID_INDEX, id)

        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.insert_user -> {
                insertUsers()
                true
            }
            R.id.delete_all ->{
                deleteAllUsers()
                true
            }
            else -> return false
        }
    }

    override fun onResume() {
        super.onResume()
        supportLoaderManager.restartLoader(USER_LOADER_ID, null, this)
    }



    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return MyTaskLoader(this)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        mAdapter.swapCursor(null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        mAdapter.swapCursor(data)
    }

    class MyTaskLoader(context: Context) : AsyncTaskLoader<Cursor>(context) {

        private var mCursor: Cursor? = null

        private val projection = arrayOf(
            UserEntry._ID,
            UserEntry.COLUMN_USERNAME,
            UserEntry.COLUMN_EMAIL,
            UserEntry.COLUMN_BALANCE)

        override fun loadInBackground(): Cursor? {

            Log.d("Query Data", "query data in background success")

            mCursor?.moveToFirst()

            return try {
                context.contentResolver.query(
                    UserEntry.CONTENT_URI,
                    projection,
                    null,
                    null,
                    null
                )

            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        override fun onStartLoading() {
            if (mCursor != null) {
                deliverResult(mCursor)
            } else forceLoad()
        }

        override fun deliverResult(data: Cursor?) {
            mCursor = data
            super.deliverResult(data)
        }

    }

}