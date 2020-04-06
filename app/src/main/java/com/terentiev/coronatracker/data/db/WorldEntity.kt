package com.terentiev.coronatracker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "world")
data class WorldEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var updated: Long,
    var cases: Int,
    var todayCases: Int,
    var deaths: Int,
    var todayDeaths: Int,
    var recovered: Int,
    var active: Int,
    var critical: Int,
    var casesPerMillion: Double,
    var deathsPerMillion: Double,
    var tests: Int,
    var testsPerMillion: Int,
    var affectedCountries: Int
)
