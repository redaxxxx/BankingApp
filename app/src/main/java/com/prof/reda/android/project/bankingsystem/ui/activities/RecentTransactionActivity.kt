package com.prof.reda.android.project.bankingsystem.ui.activities

import android.content.Context
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.loader.app.LoaderManager.LoaderCallbacks
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.prof.reda.android.project.bankingsystem.R
import com.prof.reda.android.project.bankingsystem.data.TransactionAdapter
import com.prof.reda.android.project.bankingsystem.databinding.ActivityRecentTransactionBinding
import com.prof.reda.android.project.bankingsystem.data.database.TransactionsContract.Companion.TransactionsEntry

class RecentTransactionActivity : AppCompatActivity(), LoaderCallbacks<Cursor> {
    private lateinit var binding: ActivityRecentTransactionBinding
    private lateinit var mAdapter: TransactionAdapter
    private val TRANSACTION_LOADER_ID = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recent_transaction)
        mAdapter = TransactionAdapter()

        binding.recentTransactionRv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }

        supportLoaderManager.initLoader(TRANSACTION_LOADER_ID, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return MyTask(this)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        mAdapter.swapCursor(null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        mAdapter.swapCursor(data)
    }

    private class MyTask(context: Context) : AsyncTaskLoader<Cursor>(context){

        private var cursor: Cursor? =null
        override fun loadInBackground(): Cursor? {
            cursor?.moveToFirst()

            return try {
                context.contentResolver.query(
                    TransactionsEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
                )
            }catch (e:Exception){
                e.printStackTrace()
                null
            }
        }

        override fun onStartLoading() {
            if (cursor != null){
                deliverResult(cursor)
            }else{
                forceLoad()
            }
        }

        override fun deliverResult(data: Cursor?) {
            cursor = data
            super.deliverResult(data)
        }
    }

    override fun onResume() {
        super.onResume()
        supportLoaderManager.restartLoader(TRANSACTION_LOADER_ID, null, this)
    }
}