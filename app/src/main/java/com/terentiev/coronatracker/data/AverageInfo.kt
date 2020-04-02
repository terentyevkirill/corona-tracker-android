package com.terentiev.coronatracker.data

data class AverageInfo(
    val cases: Int,
    val deaths: Int,
    val recovered: Int,
    val updated: Long,
    val active: Int,
    val affectedCountries: Int
)