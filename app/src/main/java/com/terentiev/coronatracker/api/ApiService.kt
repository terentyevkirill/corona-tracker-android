package com.terentiev.coronatracker.api

import com.terentiev.coronatracker.data.AverageInfo
import com.terentiev.coronatracker.data.Country
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("/countries?sort=cases")
    suspend fun fetchCountriesByCases(): Response<List<Country>>

    @GET("/all")
    suspend fun fetchAll(): Response<AverageInfo>
}