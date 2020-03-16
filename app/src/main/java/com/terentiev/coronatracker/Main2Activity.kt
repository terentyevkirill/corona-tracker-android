package com.terentiev.coronatracker

import android.app.SearchManager
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.terentiev.coronatracker.api.ApiService
import com.terentiev.coronatracker.data.Country
import com.terentiev.coronatracker.ui.dashboard.CountriesAdapter
import kotlinx.android.synthetic.main.activity_main2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Main2Activity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var searchView: SearchView
    private lateinit var adapter: CountriesAdapter
    private lateinit var networkUnavailableSnackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        networkUnavailableSnackbar =
            Snackbar.make(container_mainactivity2, getString(R.string.no_conn), Snackbar.LENGTH_LONG)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CountriesAdapter()
        recyclerView.adapter = adapter
        swipeRefreshLayout.setOnRefreshListener(this)
        onRefresh()
    }

    private fun loadJSON() {
        swipeRefreshLayout.isRefreshing = true

        val retrofit = Retrofit.Builder()
            .baseUrl("https://corona.lmao.ninja")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        api.fetchAllCountries().enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                Log.d("DashboardFragment", "onResponse()")
                adapter.setCountries(response.body()!!)
                swipeRefreshLayout.isRefreshing = false
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                Log.d("DashboardFragment", "onFailure()")
                swipeRefreshLayout.isRefreshing = false
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        searchView = menu?.findItem(R.id.search_item)?.actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView?.maxWidth = Int.MAX_VALUE
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.search_item -> {
//                swipeRefreshLayout.isRefreshing = false
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.isIconified = true
        } else {
            super.onBackPressed()
        }
    }

    override fun onRefresh() {
        if (isNetworkAvailable()) {
            networkUnavailableSnackbar.dismiss()
            loadJSON()
        } else {
            networkUnavailableSnackbar.show()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}
