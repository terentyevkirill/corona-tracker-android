package com.terentiev.coronatracker

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.*
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
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.*

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
    private var averageInfo: AverageInfo? = null

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
            .baseUrl("https://disease.sh/v3/covid-19/")
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
        updateFailedSnackBar = Snackbar.make(
            container_mainactivity2,
            getString(R.string.update_failed),
            Snackbar.LENGTH_LONG
        )
    }

    private fun showUpdateToast() {
        val date = Date(averageInfo!!.updated)
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
        val toast = Toast.makeText(
            applicationContext, "${getString(R.string.updated)}\n${sdf.format(date)}",
            Toast.LENGTH_LONG
        )
        ((toast.view as LinearLayout).getChildAt(0) as TextView).gravity = Gravity.CENTER_HORIZONTAL
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 200)
        toast.show()
    }

    private fun parallelRequest(
        successHandler: (Response<List<Country>>?, Response<AverageInfo>?) -> Unit,
        failureHandler: (Throwable?) -> Unit
    ) {
        CoroutineScope(CoroutineExceptionHandler { _, throwable ->
            failureHandler(throwable)
        }).launch {
            val countriesResponse = async { api.fetchCountriesByCases() }
            val worldResponse = async { api.fetchAll() }
            withContext(Dispatchers.Main) {
                successHandler(countriesResponse.await(), worldResponse.await())
            }
        }
    }

    private fun loadData() {
        // TODO: call both requests together (RxJs zip?)
        swipeRefreshLayout.isRefreshing = true
        parallelRequest(
            { countriesResponse, worldResponse ->
                if (countriesResponse!!.isSuccessful and worldResponse!!.isSuccessful) {
                    Log.d("MainActivity", "onResponseCountries():${countriesResponse.body()}")
                    Log.d("MainActivity", "onResponseWorld():${worldResponse.body()}")
                    adapter.setCountries(countriesResponse.body()!!)
                    averageInfo = worldResponse.body();
                    adapter.setAverageInfo(averageInfo!!)
                    saveDataToSharedPrefs(countriesResponse.body()!!, worldResponse.body()!!)
                    swipeRefreshLayout.isRefreshing = false
                    if (updateFailedSnackBar.isShown) {
                        updateFailedSnackBar.dismiss()
                    }
                }
            },
            { exception ->
                Log.d("MainActivity", "onFailure(): ${exception!!.cause}")
                swipeRefreshLayout.isRefreshing = false
                if (!updateFailedSnackBar.isShown) {
                    updateFailedSnackBar.show()
                }
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        searchView = menu?.findItem(R.id.search_item)?.actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                searchView.clearFocus();
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
            else -> super.onOptionsItemSelected(item!!)
        }
    }

    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.isIconified = true
            searchView.onActionViewCollapsed()
        } else {
            super.onBackPressed()
        }
    }

    override fun onRefresh() {
        if (isNetworkAvailable()) {
            networkUnavailableSnackBar.dismiss()
            recyclerView.recycledViewPool.clear()
            loadData()
        } else {
            swipeRefreshLayout.postDelayed({
                swipeRefreshLayout.isRefreshing = false
            }, 100)
            networkUnavailableSnackBar.show()
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
            averageInfo = gson.fromJson(pref.getString("all", ""), typeAll) as AverageInfo
            adapter.setCountries(countries)
            adapter.setAverageInfo(averageInfo!!)
        }
    }

    private fun saveDataToSharedPrefs(countries: List<Country>, world: AverageInfo) {
        val editor = pref.edit()
        val gson = Gson()
        editor.putString("countries", gson.toJson(countries))
        editor.putString("all", gson.toJson(world))
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
