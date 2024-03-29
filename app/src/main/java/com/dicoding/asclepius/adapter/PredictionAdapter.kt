package com.dicoding.asclepius.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.db.Prediction
import com.dicoding.asclepius.R

class PredictionAdapter(private val listener: OnDeleteClickListener) : RecyclerView.Adapter<PredictionAdapter.PredictionViewHolder>() {
    private var predictionList: List<Prediction> = emptyList()

    inner class PredictionViewHolder(itemView: View, private val listener: OnDeleteClickListener) : RecyclerView.ViewHolder(itemView) {
        val labelTextView: TextView = itemView.findViewById(R.id.textViewLabel)
        val scoreTextView: TextView = itemView.findViewById(R.id.textViewScore)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteItem)

        init {
            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val prediction = predictionList[position]
                    listener.onDeleteClick(prediction)
                }
            }
        }
    }

    fun setData(predictions: List<Prediction>) {
        predictionList = predictions
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.prediction_item, parent, false)
        return PredictionViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: PredictionViewHolder, position: Int) {
        val currentPrediction = predictionList[position]
        holder.labelTextView.text = currentPrediction.label

        val confidenceScore = currentPrediction.confidenceScore * 100
        val formattedScore = "${confidenceScore.toInt()}%"
        holder.scoreTextView.text = formattedScore

        Glide.with(holder.itemView)
            .load(currentPrediction.imageUri)
            .into(holder.imageView)
    }

    override fun getItemCount() = predictionList.size
}