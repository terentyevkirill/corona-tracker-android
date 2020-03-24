package com.terentiev.coronatracker.data

data class CountryInfo(
    val iso2: String,
    val iso3: String,
    val _id: String,
    val lat: Double,
    val long: Double,
    val flag: String
)