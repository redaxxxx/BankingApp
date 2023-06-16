package com.prof.reda.android.project.bankingsystem.ui.fragments

import android.content.Context
import android.database.Cursor
import android.os.Bundle
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
import com.prof.reda.android.project.bankingsystem.R
import com.prof.reda.android.project.bankingsystem.data.UserAdapter
import com.prof.reda.android.project.bankingsystem.data.database.UsersContract
import com.prof.reda.android.project.bankingsystem.data.database.UsersContract.Companion.UserEntry
import com.prof.reda.android.project.bankingsystem.databinding.FragmentSelectAccountBinding

class SelectAccountFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor>, UserAdapter.OnItemClickListenerDatabase {
    private lateinit var binding: FragmentSelectAccountBinding
    private val USER_LOADER_ID: Int = 0
    private lateinit var mAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_account, container, false)

        mAdapter = UserAdapter(requireContext(), this)

        binding.selectAccountRV.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }

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


    class MyTaskLoader(context:Context): AsyncTaskLoader<Cursor>(context){

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

    override fun onClick(id: Int) {
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.userDetails, TransferScreenFragment())
            ?.commit()

        Toast.makeText(activity, "Transaction Successful", Toast.LENGTH_SHORT).show()
    }
}