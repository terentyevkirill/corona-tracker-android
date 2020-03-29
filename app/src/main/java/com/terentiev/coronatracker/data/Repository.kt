package com.terentiev.coronatracker.data

import com.terentiev.coronatracker.androidmanagers.NetworkManager
import io.reactivex.Observable
import javax.inject.Inject

class Repository @Inject constructor(var networkManager: NetworkManager) {
    private val localDataSource = LocalDataSource()
    private val remoteDataSource = RemoteDataSource()

    fun getCountries(): Observable<List<CountryData>> {
        networkManager.isConnectedToInternet?.let {
            if (it) {
                return remoteDataSource.getCountries()
            }
        }

        return localDataSource.getCountries()
    }

    fun getWorld(): Observable<WorldData> {
        networkManager.isConnectedToInternet?.let {
            if (it) {
                return remoteDataSource.getWorld()
            }
        }

        return localDataSource.getWorld()
    }
}