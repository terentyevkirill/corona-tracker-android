package com.terentiev.coronatracker.api

import com.terentiev.coronatracker.data.WorldData
import com.terentiev.coronatracker.data.CountryData
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/countries?sort=cases")
    fun fetchCountriesByCases(): Call<List<CountryData>>

    @GET("/all")
    fun fetchAll(): Call<WorldData>
}