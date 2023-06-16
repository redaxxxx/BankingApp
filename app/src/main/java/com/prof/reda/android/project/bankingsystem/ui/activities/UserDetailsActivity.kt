package com.prof.reda.android.project.bankingsystem.ui.activities

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import com.prof.reda.android.project.bankingsystem.*
import com.prof.reda.android.project.bankingsystem.data.database.UserDbHelper
import com.prof.reda.android.project.bankingsystem.data.database.UsersContract
import com.prof.reda.android.project.bankingsystem.data.database.UsersContract.Companion.UserEntry
import com.prof.reda.android.project.bankingsystem.databinding.ActivityUserDetailsBinding
import com.prof.reda.android.project.bankingsystem.ui.fragments.AccountDetailsFragment

class UserDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_details)

        val getIdFromIntent = intent
        val id = getIdFromIntent.getIntExtra(KEY_ID_INDEX, 0)

        val bundle = Bundle()
        bundle.putInt(KEY_ID_INDEX, id)

        val fragment = AccountDetailsFragment()
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction().replace(R.id.userDetails, fragment).commit()
    }

}