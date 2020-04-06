package com.terentiev.coronatracker.api

import com.terentiev.coronatracker.data.WorldResponse
import com.terentiev.coronatracker.data.CountryResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("/countries?sort=cases")
    suspend fun fetchCountriesByCases(): Response<List<CountryResponse>>

    @GET("/all")
    suspend fun fetchAll(): Response<WorldResponse>
}