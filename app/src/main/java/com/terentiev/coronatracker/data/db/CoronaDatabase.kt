package com.terentiev.coronatracker.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CountryEntity::class, WorldEntity::class], version = 1, exportSchema = false)
abstract class CoronaDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao
    abstract fun worldDao(): WorldDao

    companion object {
        private var instance: CoronaDatabase? = null
        fun getInstance(context: Context): CoronaDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    CoronaDatabase::class.java,
                    "coronadb"
                ).build()
            }

            return instance as CoronaDatabase
        }
    }
}