package com.terentiev.coronatracker.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WorldDao {
    @Query("SELECT * FROM world ORDER BY id DESC LIMIT 1")
    fun getLastAdded(): WorldEntity

    @Query("SELECT * FROM world")
    fun getAll(): List<WorldEntity>

    @Insert
    fun insert(world: WorldEntity)

    @Delete
    fun delete()
}