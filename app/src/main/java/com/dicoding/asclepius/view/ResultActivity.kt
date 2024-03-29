package com.dicoding.asclepius.view

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.db.Prediction
import com.dicoding.asclepius.db.PredictionDatabase
import com.dicoding.asclepius.helper.ImageClassifierHelper
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private lateinit var imageUri: Uri
    private var label: String = ""
    private var score: Float = 0.0f
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageUri = intent.getParcelableExtra<Uri>(MainActivity.EXTRA_IMAGE) ?: return

        imageUri?.let {
            val inputStream = contentResolver.openInputStream(it)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            binding.resultImage.setImageBitmap(bitmap)

            imageClassifierHelper = ImageClassifierHelper(
                context = this,
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(error: String) {
                        runOnUiThread {
                            Toast.makeText(this@ResultActivity, "gagal melakukan analisis pada gambar", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onResults(
                        results: List<Classifications>?,
                        inferenceTime: Long
                    ) {
                        results?.let {
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                val sortedCategories =
                                    it[0].categories.sortedByDescending { it?.score }
                                val displayResult =
                                    sortedCategories.joinToString("\n") {
                                        "${it.label} " + NumberFormat.getPercentInstance()
                                            .format(it.score).trim()
                                    }
                                binding.resultText.text = displayResult

                                label = sortedCategories.first().label
                                score = sortedCategories.first().score
                            }
                        }
                    }
                }
            )

            imageClassifierHelper.classifyImage(bitmap)
        }

        binding.toggleFav.setOnCheckedChangeListener { buttonView, isChecked ->
            isFavorite = isChecked
            updateToggleButtonBackground()
            savePredictionToDatabase()
        }

        updateToggleButtonBackground()

        binding.historyButton.setOnClickListener {
            moveToHistory()
        }

        binding.backButton.setOnClickListener {
            moveToMain()
        }

        binding.analyzeButton.setOnClickListener {
            moveToMain()
        }

        binding.artikelButton.setOnClickListener {
            moveToNews()
        }
    }
    private fun updateToggleButtonBackground() {
        if (isFavorite) {
            binding.toggleFav.setBackgroundResource(R.drawable.favbuttonon)
        } else {
            binding.toggleFav.setBackgroundResource(R.drawable.favbuttonoff)
        }
    }

    private fun savePredictionToDatabase() {
        imageUri?.let { uri ->
            lifecycleScope.launch {
                if (isFavorite) {
                    val prediction = Prediction(uri.toString(), label, score)
                    PredictionDatabase.getDatabase(this@ResultActivity)?.predictionDao()?.insertPrediction(prediction)
                } else {
                    PredictionDatabase.getDatabase(this@ResultActivity)?.predictionDao()?.deletePredictionByUri(uri.toString())
                }
            }
        }
    }

    private fun moveToHistory() {
        val intent = Intent(this, SavedActivity::class.java)
        startActivity(intent)
    }

    private fun moveToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun moveToNews() {
        val intent = Intent(this, NewsActivity::class.java)
        startActivity(intent)
    }

}