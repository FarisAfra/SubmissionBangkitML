package com.dicoding.asclepius.model

import com.google.gson.annotations.SerializedName

data class Article(
    val title: String,
    val author: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    @SerializedName("publishedAt")
    val publishedAt: String
)
