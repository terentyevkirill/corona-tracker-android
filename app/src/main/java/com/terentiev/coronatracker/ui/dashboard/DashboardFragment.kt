package com.terentiev.coronatracker.ui.dashboard

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.terentiev.coronatracker.R
import com.terentiev.coronatracker.api.ApiService
import com.terentiev.coronatracker.data.CountryData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var adapter: CountriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        recyclerView.layoutManager = LinearLayoutManager(context)
//        adapter = CountriesAdapter(this)
//        recyclerView.adapter = adapter
//        loadJSON()


        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        return root
    }

    private fun loadJSON() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://corona.lmao.ninja")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)
        val progressDialog = ProgressDialog(context)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Fetching data");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show()
        api.fetchCountriesByCases().enqueue(object : Callback<List<CountryData>> {
            override fun onResponse(call: Call<List<CountryData>>, response: Response<List<CountryData>>) {
                d("DashboardFragment", "onResponse()")
                progressDialog.dismiss()
                adapter.setCountries(response.body()!!)
            }

            override fun onFailure(call: Call<List<CountryData>>, t: Throwable) {
                progressDialog.dismiss()
                d("DashboardFragment", "onFailure()")
            }

        })
    }
}
