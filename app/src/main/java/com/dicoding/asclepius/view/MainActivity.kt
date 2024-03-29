package com.dicoding.asclepius.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.yalantis.ucrop.UCrop
import java.io.File


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener { moveToResult() }
        binding.resetImageButton.setOnClickListener { resetImage() }
        binding.historyButton.setOnClickListener { moveToHistory() }
    }

    private fun startGallery() {
//        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        cropLauncher.launch(intent)
    }

    private val cropLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = result.data?.data
            if (uri != null) {
                // Memulai proses pemangkasan gambar menggunakan UCrop
                startCrop(uri)
            } else {
                Log.d("Photo Picker", "No media selected")
            }
        }
    }

    private val uCropLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val resultUri = UCrop.getOutput(result.data!!)
            if (resultUri != null) {
                // Menetapkan URI gambar yang dipangkas ke ImageView
                currentImageUri = resultUri
                showImage()
            }
        } else if (result.resultCode == UCrop.RESULT_ERROR) {
            val error = UCrop.getError(result.data!!)
            // Menampilkan pesan kesalahan jika ada
            showToast("Error: ${error?.localizedMessage}")
        }
    }

    private fun startCrop(uri: Uri) {
        val fileName = "${System.currentTimeMillis()}_cropped_image.jpg"
        val destinationUri = Uri.fromFile(File(cacheDir, fileName)) // Ganti cacheDir dengan lokasi penyimpanan yang Anda inginkan
        val uCrop = UCrop.of(uri, destinationUri)

        // Konfigurasi UCrop (opsional)
        uCrop.withAspectRatio(1f, 1f)

        // Memanggil get intent untuk mendapatkan intent dari UCrop
        val intent = uCrop.getIntent(this)

        // Meluncurkan aktivitas pemangkasan gambar dengan launcher untuk hasil kembali
        uCropLauncher.launch(intent)
    }

    private fun showImage() {
        Glide.with(this)
            .load(currentImageUri ?: R.drawable.addimage)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.previewImageView)
    }

    private fun analyzeImage() {
        // TODO: Menganalisa gambar yang berhasil ditampilkan.
    }

    private fun moveToResult() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(EXTRA_IMAGE, currentImageUri)
        startActivity(intent)
    }

    private fun moveToHistory() {
        val intent = Intent(this, SavedActivity::class.java)
        startActivity(intent)
    }

    private fun resetImage() {
        currentImageUri = null
        showImage()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_IMAGE = "com.dicoding.asclepius.view.EXTRA_IMAGE"
    }
}