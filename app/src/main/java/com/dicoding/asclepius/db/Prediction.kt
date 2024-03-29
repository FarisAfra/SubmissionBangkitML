package com.dicoding.asclepius.db

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "predictions")
data class Prediction(
    val imageUri: String,
    val label: String,
    val confidenceScore: Float
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
