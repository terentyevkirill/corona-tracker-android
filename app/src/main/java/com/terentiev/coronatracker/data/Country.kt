package com.terentiev.coronatracker.data

data class Country(
    val country: String,
    val cases: Long,
    val todayCases: Long,
    val deaths: Long,
    val todayDeaths: Long,
    val recovered: Long,
    val critical: Long
)