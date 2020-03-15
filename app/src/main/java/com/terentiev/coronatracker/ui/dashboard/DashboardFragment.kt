package com.terentiev.coronatracker.ui.dashboard

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.terentiev.coronatracker.R
import com.terentiev.coronatracker.api.ApiService
import com.terentiev.coronatracker.data.Country
import kotlinx.android.synthetic.main.fragment_dashboard.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://corona.lmao.ninja")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)
        api.fetchAllCountries().enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                d("DashboardFragment", "onResponse()")
                showData(response.body()!!)
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                d("DashboardFragment", "onFailure()")
            }

        })

        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        return root
    }

    private fun showData(countries: List<Country>) {
        countryRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CountriesAdapter(countries)
        }
    }
}
