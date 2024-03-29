package com.dicoding.asclepius.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.OnDeleteClickListener
import com.dicoding.asclepius.adapter.PredictionAdapter
import com.dicoding.asclepius.db.Prediction
import com.dicoding.asclepius.db.PredictionDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedActivity : AppCompatActivity(), OnDeleteClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PredictionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PredictionAdapter(this)

        // Ambil data dari database
        val predictionDao = PredictionDatabase.getDatabase(this)?.predictionDao()
        predictionDao?.getAllPredictions()?.observe(this, { predictions ->
            predictions?.let {
                adapter.setData(it)
            }
        })

        recyclerView.adapter = adapter
    }

    override fun onDeleteClick(prediction: Prediction) {
        // Hapus data item dari sumber data (misalnya database)
        val predictionDao = PredictionDatabase.getDatabase(this)?.predictionDao()
        CoroutineScope(Dispatchers.IO).launch {
            predictionDao?.deletePrediction(prediction)
            withContext(Dispatchers.Main) {
                // Perbarui tampilan RecyclerView
                adapter.notifyDataSetChanged()
            }
        }
    }

}