package com.terentiev.coronatracker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey
    var name: String,
    var cases: Int,
    var todayCases: Int,
    var deaths: Int,
    var todayDeaths: Int,
    var recovered: Int,
    var active: Int?,
    var critical: Int?,
    var casesPerMillion: Double?,
    var deathsPerMillion: Double?,
    var latitude: Double?,
    var longitude: Double?,
    var flag: String?,
    var iso2: String?,
    var iso3: String?,
    var tests: Int?,
    var testsPerMillion: Int?
)