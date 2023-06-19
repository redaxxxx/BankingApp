package com.prof.reda.android.project.bankingsystem.ui.fragments

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.prof.reda.android.project.bankingsystem.ENTER_AMOUNT_HERE
import com.prof.reda.android.project.bankingsystem.KEY_USERNAME_COLUMN_INDEX
import com.prof.reda.android.project.bankingsystem.R
import com.prof.reda.android.project.bankingsystem.data.UserAdapter
import com.prof.reda.android.project.bankingsystem.data.database.MyDbHelper
import com.prof.reda.android.project.bankingsystem.data.database.UsersContract
import com.prof.reda.android.project.bankingsystem.data.database.UsersContract.Companion.UserEntry

import com.prof.reda.android.project.bankingsystem.data.database.TransactionsContract.Companion.TransactionsEntry

import com.prof.reda.android.project.bankingsystem.databinding.FragmentSelectAccountBinding

class SelectAccountFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor>,
    UserAdapter.OnItemClickListenerDatabase {
    private lateinit var binding: FragmentSelectAccountBinding
    private val USER_LOADER_ID: Int = 0
    private lateinit var mAdapter: UserAdapter
    private lateinit var amountText: String
    private lateinit var senderName: String
    private lateinit var receiverName: String
    private lateinit var dbHelper: MyDbHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_account, container, false)

        dbHelper = MyDbHelper(requireContext())

        mAdapter = UserAdapter(requireContext(), this)

        val bundle = arguments
        senderName = bundle?.getString(KEY_USERNAME_COLUMN_INDEX).toString()
        amountText = bundle?.getString(ENTER_AMOUNT_HERE).toString()


        binding.selectAccountRV.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }

        val fragment = SelectAccountFragment()
        fragment.view?.isFocusableInTouchMode
        fragment.view?.requestFocus()
        fragment.view?.setOnKeyListener(View.OnKeyListener{view, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_BACK){
                insertTransaction("Failed")
                return@OnKeyListener true
            }
            return@OnKeyListener false

        })

        activity?.supportLoaderManager?.initLoader(USER_LOADER_ID, null, this)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity?.supportLoaderManager?.restartLoader(USER_LOADER_ID, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return MyTaskLoader(requireContext())
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        mAdapter.swapCursor(null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        mAdapter.swapCursor(data)
    }


    private class MyTaskLoader(context:Context): AsyncTaskLoader<Cursor>(context){

        private var mCursor:Cursor? = null

        private val projection = arrayOf(
            UsersContract.Companion.UserEntry._ID,
            UsersContract.Companion.UserEntry.COLUMN_USERNAME,
            UsersContract.Companion.UserEntry.COLUMN_EMAIL,
            UsersContract.Companion.UserEntry.COLUMN_BALANCE)

        override fun loadInBackground(): Cursor? {
            mCursor?.moveToFirst()

            return try {
                context.contentResolver.query(
                    UserEntry.CONTENT_URI,
                    projection,
                    null,
                    null,
                    null
                )

            }catch (e : java.lang.Exception){
                e.printStackTrace()
                null
            }
        }

        override fun onStartLoading() {
            if (mCursor != null){
                deliverResult(mCursor)
            }else{
                forceLoad()
            }
        }

        override fun deliverResult(data: Cursor?) {
            mCursor = data
            super.deliverResult(data)
        }
    }

    private fun insertTransaction(status:String){
        Log.v("Banking System", "Sender Name: $senderName")
        Log.v("Banking System", "Receiver Name: $receiverName")
        Log.v("Banking System", "Amount : $amountText")
        val values = ContentValues()

        values.put(TransactionsEntry.COLUMN_SENDER_NAME, senderName)
        values.put(TransactionsEntry.COLUMN_RECEIVER_NAME, receiverName)
        values.put(TransactionsEntry.COLUMN_AMOUNT, amountText)
        values.put(TransactionsEntry.COLUMN_STATUS, status)

        val uri = context?.contentResolver?.insert(TransactionsEntry.CONTENT_URI, values)
    }

    override fun onClick(id: Int) {

        val database = dbHelper.writableDatabase
        val cursor = database.rawQuery(
            "SELECT * FROM " + UsersContract.Companion.UserEntry.TABLE_NAME + " WHERE " +
                    UsersContract.Companion.UserEntry._ID +
                    "= " + id, null
        )

        cursor.moveToFirst()
        val nameColumnIndex: Int = cursor.getColumnIndex(UsersContract.Companion.UserEntry.COLUMN_USERNAME)

        receiverName = cursor.getString(nameColumnIndex)
        insertTransaction("Successful")



        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.userDetails, TransferScreenFragment())
            ?.commit()


        Toast.makeText(activity, "Transaction Successful", Toast.LENGTH_SHORT).show()
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true){
//            override fun handleOnBackPressed() {
//                insertTransaction("Failed")
//            }
//        }
//
//        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
//    }
}