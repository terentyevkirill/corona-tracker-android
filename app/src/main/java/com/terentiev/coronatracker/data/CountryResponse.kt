package com.terentiev.coronatracker.data

import com.terentiev.coronatracker.data.db.CountryEntity

data class CountryResponse(
    val country: String,
    val countryInfo: CountryInfo,
    val updated: Long,
    val cases: Int,
    val todayCases: Int,
    val deaths: Int,
    val todayDeaths: Int,
    val recovered: Int,
    val active: Int?,
    val critical: Int?,
    val casesPerOneMillion: Double?,
    val deathsPerOneMillion: Double?,
    val tests: Int?,
    val testsPerOneMillion: Int?
) {
    fun mapToEntity(): CountryEntity {
        val flag: String? = if (countryInfo.flag.contains("unknow"))
            null
        else
            countryInfo.flag

        return CountryEntity(
            country,
            cases,
            todayCases,
            deaths,
            todayDeaths,
            recovered,
            active,
            critical,
            casesPerOneMillion,
            deathsPerOneMillion,
            countryInfo.lat,
            countryInfo.long,
            flag,
            countryInfo.iso2,
            countryInfo.iso3,
            tests,
            testsPerOneMillion
        )
    }
}