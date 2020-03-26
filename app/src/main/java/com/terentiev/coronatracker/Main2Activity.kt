package com.terentiev.coronatracker

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.terentiev.coronatracker.api.ApiService
import com.terentiev.coronatracker.data.AverageInfo
import com.terentiev.coronatracker.data.Country
import com.terentiev.coronatracker.ui.dashboard.CountriesAdapter
import kotlinx.android.synthetic.main.activity_main2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class Main2Activity : AppCompatActivity(),
    SwipeRefreshLayout.OnRefreshListener,
    CountriesAdapter.ItemEvents {

    private lateinit var searchView: SearchView
    private lateinit var adapter: CountriesAdapter
    private lateinit var networkUnavailableSnackBar: Snackbar
    private lateinit var updateFailedSnackBar: Snackbar
    private lateinit var pref: SharedPreferences
    private lateinit var retrofit: Retrofit
    private lateinit var api: ApiService
    private var averageData: AverageInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        initSnackBars()
        initRecyclerView()
        initApi()
        initSwipeRefreshLayout()
        loadDataFromSharedPrefs()
        onRefresh()
    }

    private fun initApi() {
        retrofit = Retrofit.Builder()
            .baseUrl("https://corona.lmao.ninja")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(ApiService::class.java)
    }

    private fun initSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(
            R.color.colorPrimary
        )
        swipeRefreshLayout.setOnRefreshListener(this)
    }

    private fun initRecyclerView() {
        adapter = CountriesAdapter(this)
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                checkEmpty()
            }

            fun checkEmpty() {
                tv_no_results.visibility = (if (adapter.itemCount == 0) View.VISIBLE else View.GONE)
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun initSnackBars() {
        networkUnavailableSnackBar =
            Snackbar.make(
                container_mainactivity2,
                getString(R.string.no_conn),
                Snackbar.LENGTH_LONG
            )
        networkUnavailableSnackBar.setAction(R.string.settings) {
            startActivity(Intent(Settings.ACTION_SETTINGS))
        }
        updateFailedSnackBar = Snackbar.make(container_mainactivity2, getString(R.string.update_failed), Snackbar.LENGTH_LONG)
    }

    private fun showUpdateToast() {
        val date = Date(averageData!!.updated)
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
        val toast = Toast.makeText(
            applicationContext, "${getString(R.string.updated)}\n${sdf.format(date)}",
            Toast.LENGTH_LONG
        )
        ((toast.view as LinearLayout).getChildAt(0) as TextView).gravity = Gravity.CENTER_HORIZONTAL
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 200)
        toast.show()
    }

    private fun loadJSON() {
        swipeRefreshLayout.isRefreshing = true
        // TODO: call both requests together (RxJs zip?)
        api.fetchAllCountries().enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    Log.d("MainActivity", "onResponse():${response.body()}")
                    adapter.setCountries(response.body()!!)
                    saveDataToSharedPrefs(response.body()!!)
                    swipeRefreshLayout.isRefreshing = false
                    if (updateFailedSnackBar.isShown) {
                        updateFailedSnackBar.dismiss()
                    }
                }
            }
            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                Log.d("MainActivity", "onFailure(): ${t.cause}")
                swipeRefreshLayout.isRefreshing = false
                if (!updateFailedSnackBar.isShown) {
                    updateFailedSnackBar.show()
                }
            }
        })

        api.fetchAll().enqueue(object : Callback<AverageInfo> {
            override fun onResponse(call: Call<AverageInfo>, response: Response<AverageInfo>) {
                if (response.isSuccessful) {
                    Log.d("MainActivity", "onResponse():${response.body()}")
                    averageData = response.body()!!
                    saveDataToSharedPrefs(averageData!!)
                    showUpdateToast()
                }
            }

            override fun onFailure(call: Call<AverageInfo>, t: Throwable) {
                Log.d("MainActivity", "onFailure(): ${t.cause}")
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
            R.id.search_item -> true
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
            networkUnavailableSnackBar.dismiss()
            recyclerView.recycledViewPool.clear()
            loadJSON()
        } else {
            swipeRefreshLayout.postDelayed({
                swipeRefreshLayout.isRefreshing = false
            }, 500)
            networkUnavailableSnackBar.show()
            if (averageData != null) {
                showUpdateToast()
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun loadDataFromSharedPrefs() {
        pref = applicationContext.getSharedPreferences("MyPrefs", 0)
        if (!pref.getString("countries", "").equals("")
            && !pref.getString("all", "").equals("")
        ) {
            val gson = Gson()
            val typeCountries: Type =
                object : TypeToken<List<Country?>?>() {}.type
            val typeAll: Type =
                object : TypeToken<AverageInfo?>() {}.type
            val countries =
                gson.fromJson(pref.getString("countries", ""), typeCountries) as List<Country>
            averageData = gson.fromJson(pref.getString("all", ""), typeAll) as AverageInfo
            adapter.setCountries(countries)
        }
    }

    private fun saveDataToSharedPrefs(data: List<Country>) {
        val editor = pref.edit()
        val gson = Gson()
        editor.putString("countries", gson.toJson(data))
        editor.apply()
    }

    private fun saveDataToSharedPrefs(data: AverageInfo) {
        val editor = pref.edit()
        val gson = Gson()
        editor.putString("all", gson.toJson(data))
        editor.apply()
    }

    override fun onItemLongClicked(country: Country) {
        Log.d("MainActivity", "onItemLongClicked()")
        showUpdateToast()
    }

    override fun onItemClicked(position: Int, country: Country) {
        Log.d("MainActivity", "onItemClicked()")
    }

}
