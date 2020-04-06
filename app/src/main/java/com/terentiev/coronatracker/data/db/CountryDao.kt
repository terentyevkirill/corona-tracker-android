package com.terentiev.coronatracker.data.db

import androidx.room.*

@Dao
interface CountryDao {

    @Query("SELECT * FROM countries ORDER BY cases DESC")
    fun getAllByCasesDesc(): List<CountryEntity>

    @Query("SELECT * FROM countries ORDER BY name")
    fun getAllName(): List<CountryEntity>

    @Query("SELECT * FROM countries WHERE name LIKE :name")
    fun getByName(name: String): List<CountryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(countries: List<CountryEntity>)

    @Delete
    fun delete()
}