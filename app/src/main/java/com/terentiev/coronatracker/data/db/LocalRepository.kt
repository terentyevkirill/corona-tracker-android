package com.terentiev.coronatracker.data.db

import android.app.Application
import com.terentiev.coronatracker.data.db.CoronaDatabase
import com.terentiev.coronatracker.data.db.CountryDao
import com.terentiev.coronatracker.data.db.CountryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class LocalRepository(application: Application) : CoroutineScope {

    private var countryDao: CountryDao
    private var worldDao: WorldDao

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main


    init {
        val db = CoronaDatabase.getInstance(application)
        countryDao = db.countryDao()
        worldDao = db.worldDao()
    }

    fun getCountries() = countryDao.getAllByCasesDesc()

    fun getCountry(name: String) = countryDao.getByName(name)

    fun insertCountries(countries: List<CountryEntity>) {
        launch { insertCountriesAsync(countries) }
    }

    private suspend fun insertCountriesAsync(countries: List<CountryEntity>) {
        withContext(Dispatchers.IO) {
            countryDao.insert(countries)
        }
    }

    private suspend fun deleteCountriesAsync() {
        withContext(Dispatchers.IO) {
            countryDao.delete()
        }
    }

    fun getWorldLastUpdated() = worldDao.getLastAdded()

    fun getWorldAll() = worldDao.getAll()

    fun insertWorld(world: WorldEntity) {
        launch { insertWorldAsync(world) }
    }

    fun deleteWorldAll() {
        launch { deleteWorldAllAsync() }
    }

    private suspend fun insertWorldAsync(world: WorldEntity) {
        withContext(Dispatchers.IO) {
            worldDao.insert(world)
        }
    }

    private suspend fun deleteWorldAllAsync() {
        withContext(Dispatchers.IO) {
            worldDao.delete()
        }
    }
}