package com.prof.reda.android.project.bankingsystem.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.prof.reda.android.project.bankingsystem.*
import com.prof.reda.android.project.bankingsystem.data.database.UserDbHelper
import com.prof.reda.android.project.bankingsystem.data.database.UsersContract
import com.prof.reda.android.project.bankingsystem.data.database.UsersContract.Companion.UserEntry
import com.prof.reda.android.project.bankingsystem.databinding.ActivityUserDetailsBinding

class UserDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailsBinding
    private lateinit var userDbHelper: UserDbHelper

    private lateinit var username: String
    private lateinit var email: String
    private lateinit var balance: String
    private lateinit var phoneNumber: String
    private lateinit var cardNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_details)

        userDbHelper = UserDbHelper(this)

        val getUserInfoFromIntent = intent
        getSingleUserInfo(getUserInfoFromIntent.getIntExtra(KEY_ID_INDEX, 0))

        binding.nameTextView.text = username
        binding.emailTextView.text = email
        binding.balanceTextView.text = balance
        binding.phoneTextView.text = phoneNumber
        binding.cardNumberTextView.text = cardNumber

        binding.transferBtn.setOnClickListener {

        }
    }

    private fun getSingleUserInfo(id: Int) {
        val database = userDbHelper.writableDatabase
        val cursor = database.rawQuery(
            "SELECT * FROM " + UsersContract.Companion.UserEntry.TABLE_NAME + " WHERE " +
                    UsersContract.Companion.UserEntry._ID +
                    "= " + id, null
        )

        cursor.moveToFirst()
        val nameColumnIndex: Int = cursor.getColumnIndex(UserEntry.COLUMN_USERNAME)
        val emailColumnIndex: Int = cursor.getColumnIndex(UserEntry.COLUMN_EMAIL)
        val balanceColumnIndex: Int = cursor.getColumnIndex(UserEntry.COLUMN_BALANCE)
        val phoneNumberColumnIndex = cursor.getColumnIndex(UserEntry.COLUMN_PHONE_NUMBER)
        val cardNumberColumnIndex = cursor.getColumnIndex(UserEntry.COLUMN_CARD_NUMBER)

        username = cursor.getString(nameColumnIndex)
        email = cursor.getString(emailColumnIndex)
        balance = cursor.getString(balanceColumnIndex)
        phoneNumber = cursor.getString(phoneNumberColumnIndex)
        cardNumber = cursor.getString(cardNumberColumnIndex)


        Log.v("Banking System", "username is $username")


    }


}