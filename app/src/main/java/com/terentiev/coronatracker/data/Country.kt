package com.terentiev.coronatracker.data

data class Country(
    val updated: Long,
    val country: String,
    val countryInfo: CountryInfo,
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
    val testsPerOneMillion: Double?
)