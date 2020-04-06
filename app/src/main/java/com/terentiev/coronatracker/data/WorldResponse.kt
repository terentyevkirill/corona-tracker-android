package com.terentiev.coronatracker.data

import com.terentiev.coronatracker.data.db.WorldEntity

data class WorldResponse(
    val updated: Long,
    val cases: Int,
    val todayCases: Int,
    val deaths: Int,
    val todayDeaths: Int,
    val recovered: Int,
    val active: Int,
    val critical: Int,
    val casesPerOneMillion: Double,
    val deathsPerOneMillion: Double,
    val tests: Int,
    val testsPerOneMillion: Int,
    val affectedCountries: Int
) {
    fun mapToEntity() : WorldEntity {
        return WorldEntity(
            -1,
            updated,
            cases,
            todayCases,
            deaths,
            todayDeaths,
            recovered,
            active,
            critical,
            casesPerOneMillion,
            deathsPerOneMillion,
            tests,
            testsPerOneMillion,
            affectedCountries
        )
    }

}