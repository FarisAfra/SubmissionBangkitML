package com.dicoding.asclepius.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Prediction::class], version = 1)
abstract class PredictionDatabase : RoomDatabase() {

    abstract fun predictionDao(): PredictionDao
    companion object{
        @Volatile
        var INSTANCE : PredictionDatabase? = null

        fun getDatabase(context: Context): PredictionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PredictionDatabase::class.java,
                    "prediction_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}