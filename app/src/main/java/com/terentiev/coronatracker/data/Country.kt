package com.terentiev.coronatracker.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("country")
    @Expose
    val country: String,
    @SerializedName("countryInfo")
    @Expose
    val countryInfo: CountryInfo,
    @SerializedName("cases")
    @Expose
    val cases: Long,
    @SerializedName("todayCases")
    @Expose
    val todayCases: Long,
    @SerializedName("deaths")
    @Expose
    val deaths: Long,
    @SerializedName("todayDeaths")
    @Expose
    val todayDeaths: Long,
    @SerializedName("recovered")
    @Expose
    val recovered: Long,
    @SerializedName("active")
    @Expose
    val active: Long,
    @SerializedName("critical")
    @Expose
    val critical: Long,
    @SerializedName("casesPerOneMillion")
    @Expose
    val casesPerOneMillion: Long
)