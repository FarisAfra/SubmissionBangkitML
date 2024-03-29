package com.dicoding.asclepius.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PredictionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrediction(prediction: Prediction)

    @Query("DELETE FROM predictions WHERE imageUri = :uri")
    suspend fun deletePredictionByUri(uri: String)

    @Query("SELECT * FROM predictions")
    fun getAllPredictions(): LiveData<List<Prediction>>

    @Query("SELECT * FROM predictions WHERE imageUri = :uri")
    suspend fun getPredictionByUri(uri: String): Prediction?

    @Delete
    suspend fun deletePrediction(prediction: Prediction)
}