package com.terentiev.coronatracker.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CountryInfo(
    @SerializedName("iso2")
    @Expose
    val iso2: String,
    @SerializedName("iso3")
    @Expose
    val iso3: String,
    @SerializedName("_id")
    @Expose
    val _id: String,
    @SerializedName("lat")
    @Expose
    val lat: Double,
    @SerializedName("long")
    @Expose
    val long: Double,
    @SerializedName("flag")
    @Expose
    val flag: String
)