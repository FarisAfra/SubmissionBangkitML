package com.dicoding.asclepius.view

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri = intent.getParcelableExtra<Uri>(MainActivity.EXTRA_IMAGE)

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
                                println(it)
                                val sortedCategories =
                                    it[0].categories.sortedByDescending { it?.score }
                                val displayResult =
                                    sortedCategories.joinToString("\n") {
                                        "${it.label} " + NumberFormat.getPercentInstance()
                                            .format(it.score).trim()
                                    }
                                binding.resultText.text = displayResult
                            }
                        }
                    }
                }
            )

            imageClassifierHelper.classifyImage(bitmap)
        }

        binding.toggleFav.setOnCheckedChangeListener { buttonView, isChecked ->
            isFavorite = isChecked
            // Sesuaikan tampilan ToggleButton sesuai dengan keadaan favorit
            updateToggleButtonBackground()
        }

        updateToggleButtonBackground()
    }
    private fun updateToggleButtonBackground() {
        if (isFavorite) {
            // Jika favorit, atur gambar latar belakang menjadi gambar saat aktif
            binding.toggleFav.setBackgroundResource(R.drawable.favbuttonon)
        } else {
            // Jika tidak favorit, atur gambar latar belakang menjadi gambar saat tidak aktif
            binding.toggleFav.setBackgroundResource(R.drawable.favbuttonoff)
        }
    }
}