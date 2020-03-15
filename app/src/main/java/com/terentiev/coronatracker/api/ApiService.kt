package com.terentiev.coronatracker.api

import com.terentiev.coronatracker.data.Country
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/countries")
    fun fetchAllCountries(): Call<List<Country>>
}