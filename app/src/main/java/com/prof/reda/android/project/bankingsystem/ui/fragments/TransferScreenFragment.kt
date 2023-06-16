package com.prof.reda.android.project.bankingsystem.ui.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.prof.reda.android.project.bankingsystem.R
import com.prof.reda.android.project.bankingsystem.databinding.FragmentTransferScreenBinding
import kotlin.concurrent.thread

class TransferScreenFragment : Fragment() {

    private lateinit var binding: FragmentTransferScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transfer_screen, container, false)

        Handler().postDelayed(
            {
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.userDetails, TransactionSuccessfulFragment())
                    ?.commit()
            }
        , 1500)

        return binding.root
    }

}