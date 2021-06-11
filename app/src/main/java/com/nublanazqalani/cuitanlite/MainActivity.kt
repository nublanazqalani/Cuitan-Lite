package com.nublanazqalani.cuitanlite

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nublanazqalani.cuitanlite.model.Tweet
import com.nublanazqalani.cuitanlite.utils.Commons
import com.nublanazqalani.cuitanlite.utils.InputDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_tweet.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var vm: MainViewModel
    private lateinit var adapter: RvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutManager = LinearLayoutManager(this)
        recyclerview.layoutManager = layoutManager

        adapter = RvAdapter(){ item, _ ->
            val options = resources.getStringArray(R.array.option_edit_delete)
            Commons.showSelector(this, "Choose action", options) { _, i ->
                when (i) {
                    0 -> showDetailsDialog(item)
                    1 -> showEditDialog(item)
                    2 -> showDeleteDialog(item)
                }
            }
        }

        recyclerview.adapter = adapter

        vm = ViewModelProvider(this).get(MainViewModel::class.java)

        swiperefreshlayout.setOnRefreshListener {
            refreshData()
        }

        fab.setOnClickListener {
            showInsertDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        observeData()
    }

    private fun observeData(){
        vm.getTweets()?.observe(this, Observer {
            adapter.setTweetList(it)
            setProgressbarVisibility(false)
        })
    }

    private fun refreshData(){
        setProgressbarVisibility(true)
        observeData()
        swiperefreshlayout.isRefreshing = false
    }

    private fun showInsertDialog(){
        val view = LayoutInflater.from(this).inflate(R.layout.fragment_tweet, null)

        val dialogTitle = "Cuitan Baru"
        val toastMessage = "Cuitan berhasil dibuat"
        val failAlertMessage = "Anda belum mengisi cuitan"

        InputDialog(this, dialogTitle, view){
            val title = view.input_tweet.text.toString().trim()

            if (title == "") {
                AlertDialog.Builder(this).setMessage(failAlertMessage).setCancelable(false)
                    .setPositiveButton("OK") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.create().show()
            } else {
                val currentDate = Commons.getCurrentDateTime()
                val dateCreated =Commons.formatDate(currentDate, "dd/MM/yy HH:mm:ss")

                val tweet = Tweet(
                    tweet = title,
                    dateCreated = dateCreated,
                    dateUpdated = dateCreated)

                vm.insertTweet(tweet)

                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
            }
        }.show()
    }

    private fun showEditDialog(tweet: Tweet) {
        val view = LayoutInflater.from(this).inflate(R.layout.fragment_tweet, null)

        view.input_tweet.setText(tweet.tweet)

        val dialogTitle = "Ubah Cuitan"
        val toastMessage = "Cuitan berhasil diubah"
        val failAlertMessage = "Anda belum mengisi cuitan"

        InputDialog(this, dialogTitle, view){
            val title = view.input_tweet.text.toString().trim()

            val dateCreated = tweet.dateCreated

            if (title == "") {
                AlertDialog.Builder(this).setMessage(failAlertMessage).setCancelable(false)
                    .setPositiveButton("OK") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.create().show()
            } else {

                val currentDate = Commons.getCurrentDateTime()
                val dateUpdated =Commons.formatDate(currentDate, "dd/MM/yy HH:mm:ss")

                tweet.tweet = title
                tweet.dateCreated = dateCreated
                tweet.dateUpdated = dateUpdated

                vm.updateTweet(tweet)

                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
            }
        }.show()
    }

    private fun showDeleteDialog(tweet: Tweet) {
        vm.deleteTweet(tweet)
        Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
    }

    private fun showDetailsDialog(tweet: Tweet) {
        val title = "Title: ${tweet.tweet}"
        val dateCreated = "Date created: ${tweet.dateCreated}"
        val dateUpdated = "Date updated: ${tweet.dateUpdated}"

        val strMessage = "$title\n$dateCreated\n$dateUpdated"

        AlertDialog.Builder(this).setMessage(strMessage).setCancelable(false)
            .setPositiveButton("OK") { dialogInterface, _ ->
                dialogInterface.cancel()
            }.create().show()
    }

    private fun setProgressbarVisibility(state: Boolean) {
        if (state) progressbar.visibility = View.VISIBLE
        else progressbar.visibility = View.INVISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = (menu.findItem(R.id.menu_search_toolbar)).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Cari cuitan"
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                adapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
}
