package com.dicoding.asclepius.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.NewsAdapter
import com.dicoding.asclepius.databinding.ActivityNewsBinding
import com.dicoding.asclepius.viewmodel.NewsViewModel

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var adapter: NewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = NewsAdapter(this)
        adapter.notifyDataSetChanged()
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(NewsViewModel::class.java)

        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@NewsActivity)
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter

        }

        binding.backButton.setOnClickListener { moveToMain() }

        viewModel.getTopHeadlines().observe(this, { newsList ->
            adapter.setlist(ArrayList(newsList))
            binding.progressBar.visibility = View.GONE
        })

        viewModel.isLoading.observe(this, { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        viewModel.setTopHeadlines("cancer", "health", "en", "45655b5b89e541ceb12ac64737ff768b")
    }

    private fun moveToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}