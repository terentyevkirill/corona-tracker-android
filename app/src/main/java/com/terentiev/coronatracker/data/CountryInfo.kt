package com.terentiev.coronatracker.data

data class CountryInfo(
    val _id: String,
    val lat: Double,
    val long: Double,
    val flag: String,
    val iso3: String,
    val iso2: String
)