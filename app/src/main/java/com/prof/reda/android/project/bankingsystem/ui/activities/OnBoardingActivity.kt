package com.prof.reda.android.project.bankingsystem.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.prof.reda.android.project.bankingsystem.R
import com.prof.reda.android.project.bankingsystem.databinding.ActivityOnBoardingBinding

class OnBoardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
        val binding:ActivityOnBoardingBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_on_boarding
        )
        binding.startBtn.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

    }
}