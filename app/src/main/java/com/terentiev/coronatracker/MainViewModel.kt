package com.terentiev.coronatracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.terentiev.coronatracker.data.db.CountryEntity
import com.terentiev.coronatracker.data.db.LocalRepository
import com.terentiev.coronatracker.data.db.WorldEntity

class MainViewModel (application: Application) : AndroidViewModel(application) {

    private var repo = LocalRepository(application)

    fun getCountries() = repo.getCountries()

    fun getWorld() = repo.getWorldLastUpdated()

    fun updateCountries(countries: List<CountryEntity>) {
        repo.insertCountries(countries)
    }

    fun updateWorld(world: WorldEntity) {
        repo.insertWorld(world)
    }
}