package com.terentiev.coronatracker.api

import com.terentiev.coronatracker.data.AverageInfo
import com.terentiev.coronatracker.data.Country
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/countries?sort=cases")
    fun fetchCountriesByCases(): Call<List<Country>>

    @GET("/all")
    fun fetchAll(): Call<AverageInfo>
}