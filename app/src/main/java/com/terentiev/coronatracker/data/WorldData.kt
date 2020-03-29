package com.terentiev.coronatracker.data

data class WorldData(
    val cases: Long,
    val deaths: Long,
    val recovered: Long,
    val updated: Long,
    val active: Long
)