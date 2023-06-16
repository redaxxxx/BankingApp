package com.prof.reda.android.project.bankingsystem.ui.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import com.prof.reda.android.project.bankingsystem.ENTER_AMOUNT_HERE
import com.prof.reda.android.project.bankingsystem.KEY_ID_INDEX
import com.prof.reda.android.project.bankingsystem.R
import com.prof.reda.android.project.bankingsystem.data.database.UserDbHelper
import com.prof.reda.android.project.bankingsystem.data.database.UsersContract
import com.prof.reda.android.project.bankingsystem.databinding.FragmentAccountDetailsBinding
import com.prof.reda.android.project.bankingsystem.ui.activities.UserDetailsActivity

class AccountDetailsFragment : Fragment(){

    private lateinit var binding: FragmentAccountDetailsBinding
    private lateinit var userDbHelper: UserDbHelper

    private lateinit var username: String
    private lateinit var email: String
    private lateinit var balance: String
    private lateinit var phoneNumber: String
    private lateinit var cardNumber: String
    private var mText:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_details, container, false)

        userDbHelper = UserDbHelper(requireContext())

        val bundle = arguments
        getSingleUserInfo(bundle?.getInt(KEY_ID_INDEX, 0))

        binding.nameTextView.text = username
        binding.emailTextView.text = email
        binding.balanceTextView.text = balance
        binding.phoneTextView.text = phoneNumber
        binding.cardNumberTextView.text = cardNumber

        binding.transferBtn.setOnClickListener {
            createInputTextDialog()
        }

        return binding.root
    }

    private fun createInputTextDialog(){
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.enter_amount)

        //setup the input
        val inputEditText = EditText(activity)
        // Specify the type of input expected;
        inputEditText.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(inputEditText)

        //setup the buttons
        builder.setPositiveButton("Send", DialogInterface.OnClickListener(){ dialog, _ ->
            mText = inputEditText.text.toString()

            val fragment = SelectAccountFragment()
            val bundle = Bundle()
            bundle.putString(ENTER_AMOUNT_HERE, mText)
            fragment.arguments = bundle
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.userDetails, fragment)
                ?.addToBackStack(null)
                ?.commit()
        })

        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener(){ dialog, _ ->
            dialog.cancel()
        })

        builder.show()
    }

    private fun getSingleUserInfo(id: Int?) {
        val database = userDbHelper.writableDatabase
        val cursor = database.rawQuery(
            "SELECT * FROM " + UsersContract.Companion.UserEntry.TABLE_NAME + " WHERE " +
                    UsersContract.Companion.UserEntry._ID +
                    "= " + id, null
        )

        cursor.moveToFirst()
        val nameColumnIndex: Int = cursor.getColumnIndex(UsersContract.Companion.UserEntry.COLUMN_USERNAME)
        val emailColumnIndex: Int = cursor.getColumnIndex(UsersContract.Companion.UserEntry.COLUMN_EMAIL)
        val balanceColumnIndex: Int = cursor.getColumnIndex(UsersContract.Companion.UserEntry.COLUMN_BALANCE)
        val phoneNumberColumnIndex = cursor.getColumnIndex(UsersContract.Companion.UserEntry.COLUMN_PHONE_NUMBER)
        val cardNumberColumnIndex = cursor.getColumnIndex(UsersContract.Companion.UserEntry.COLUMN_CARD_NUMBER)

        username = cursor.getString(nameColumnIndex)
        email = cursor.getString(emailColumnIndex)
        balance = cursor.getString(balanceColumnIndex)
        phoneNumber = cursor.getString(phoneNumberColumnIndex)
        cardNumber = cursor.getString(cardNumberColumnIndex)


        Log.v("Banking System", "username is $username")


    }

}