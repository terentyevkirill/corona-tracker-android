package com.terentiev.coronatracker.data

data class AverageInfo(
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
    val testsPerOneMillion: Double,
    val affectedCountries: Int
)