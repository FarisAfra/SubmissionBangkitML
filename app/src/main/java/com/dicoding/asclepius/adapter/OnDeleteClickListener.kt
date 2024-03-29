package com.dicoding.asclepius.adapter

import com.dicoding.asclepius.db.Prediction

interface OnDeleteClickListener {
    fun onDeleteClick(prediction: Prediction)
}