package com.terentiev.coronatracker.data.db

import android.app.Application
import android.util.Log
import com.terentiev.coronatracker.api.ApiService
import com.terentiev.coronatracker.data.CountryResponse
import com.terentiev.coronatracker.data.WorldResponse
import kotlinx.coroutines.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext

class RemoteRepository(application: Application) : CoroutineScope {

    private val api: ApiService

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://corona.lmao.ninja")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(ApiService::class.java)
    }

    private fun parallelRequest(
        successHandler: (Response<List<CountryResponse>>?, Response<WorldResponse>) -> Unit,
        failureHandler: (Throwable) -> Unit
    ) {
        CoroutineScope(CoroutineExceptionHandler { _, throwable ->
            failureHandler(throwable)
        }).launch {
            withContext(Dispatchers.IO) {
                val countriesResponse = async { api.fetchCountriesByCases() }
                val worldResponse = async { api.fetchAll() }
                withContext(Dispatchers.Main) {
                    successHandler(countriesResponse.await(), worldResponse.await())
                }
            }
        }
    }

    fun getData(): Pair<WorldEntity, List<CountryEntity>> {
        lateinit var countries: ArrayList<CountryEntity>
        lateinit var world: WorldEntity
        parallelRequest(
            { countriesResponse, worldResponse ->
                if (countriesResponse!!.isSuccessful and worldResponse!!.isSuccessful) {
                    Log.d("RemoteRepository", "onResponseCountries():${countriesResponse.body()}")
                    Log.d("RemoteRepository", "onResponseWorld():${worldResponse.body()}")
                    countries = ArrayList()
                    countriesResponse.body()!!
                        .forEach { response -> countries.add(response.mapToEntity()) }
                    world = worldResponse.body()!!.mapToEntity()
                }
            },
            { exception ->
                Log.d("RemoteRepository", "onFailure(): ${exception.cause}")
            })

        return Pair(world, countries)
    }

}