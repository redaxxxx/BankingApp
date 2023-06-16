package com.prof.reda.android.project.bankingsystem.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.databinding.DataBindingUtil
import com.prof.reda.android.project.bankingsystem.R
import com.prof.reda.android.project.bankingsystem.databinding.FragmentTransactionSuccessfulBinding
import com.prof.reda.android.project.bankingsystem.ui.activities.HomeActivity

class TransactionSuccessfulFragment : Fragment() {

    private lateinit var binding: FragmentTransactionSuccessfulBinding
    private var progress = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transaction_successful, container, false)

        binding.checkImg.visibility = View.GONE

        Thread {
            kotlin.run {
                startProgress()
            }
        }.start()

        binding.returnBtn.setOnClickListener{
            startActivity(Intent(activity, HomeActivity::class.java))
        }

        return binding.root
    }

    private fun startProgress() {

        for (i in 0..9){
            progress += 10
            try {
                Thread.sleep(100)
                binding.progressBar.progress = progress
            }catch (i: InterruptedException){
                i.printStackTrace()
            }

            activity?.runOnUiThread { kotlin.run {
                if (progress == 100){
                    binding.checkImg.visibility = View.VISIBLE
                }
            } }
        }

    }


}