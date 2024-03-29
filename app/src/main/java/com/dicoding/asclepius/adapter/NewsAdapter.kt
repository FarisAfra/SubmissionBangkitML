package com.dicoding.asclepius.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.databinding.NewsItemBinding
import com.dicoding.asclepius.model.Article

class NewsAdapter(private val context: Context) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private val list = ArrayList<Article>()

    fun setlist(articles: ArrayList<Article>){
        list.clear()
        list.addAll(articles.filter { it.title != "[Removed]" && it.description != "[Removed]" && it.urlToImage != null })
        notifyDataSetChanged()
    }
    inner class NewsViewHolder(val binding: NewsItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(article: Article){
            binding.apply {
                Glide.with(itemView)
                    .load(article.urlToImage)
                    .centerCrop()
                    .into(ivArtikel)

                tvTitle.text = article.title
                tvAuthor.text = article.author
                tvPublish.text = article.publishedAt
                tvDescription.text = article.description

                detailArtikel.setOnClickListener {
                    val articleUrl = article.url
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl))
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(list[position])
    }

}