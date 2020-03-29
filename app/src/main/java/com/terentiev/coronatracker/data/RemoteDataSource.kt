package com.terentiev.coronatracker.data

import io.reactivex.Observable

class RemoteDataSource {

    fun getCountries(): Observable<List<CountryData>> {
        // get countries data from API
        return Observable.just(listOf())
    }

    fun getWorld(): Observable<WorldData> {
        return Observable.just(WorldData(1, 1, 1, 1,1))
    }
}