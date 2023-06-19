package com.prof.reda.android.project.bankingsystem.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.prof.reda.android.project.bankingsystem.*
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