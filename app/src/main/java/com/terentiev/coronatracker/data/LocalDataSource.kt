package com.terentiev.coronatracker.data

import io.reactivex.Observable

class LocalDataSource {
    fun getCountries(): Observable<List<CountryData>> {
        // Retrieve countries data from DB
        return Observable.just(listOf())
    }

    fun getWorld(): Observable<WorldData> {
        // retrieve world data from DB
        return Observable.just(WorldData(1, 1, 1, 1,1))
    }
}