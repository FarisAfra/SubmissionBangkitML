package com.dicoding.asclepius.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.model.Article
import com.dicoding.asclepius.model.NewsResponse
import com.dicoding.asclepius.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel : ViewModel() {

    val listNews = MutableLiveData<List<Article>>()
    val isLoading = MutableLiveData<Boolean>()

    fun setTopHeadlines(query: String, category: String, language: String, apikey: String ) {
        isLoading.value = true

        ApiClient.apiInstance
            .getTopHeadlines("cancer", "health", "en", "45655b5b89e541ceb12ac64737ff768b")
            .enqueue(object : Callback<NewsResponse>{
                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {
                    if (response.isSuccessful){
                        listNews.postValue(response.body()?.articles)
                    }
                    isLoading.postValue(false)
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    Log.e("MainActivity", "onFailure: ${t.message}")
                    isLoading.postValue(false)
                }

            })
    }

    fun getTopHeadlines(): LiveData<List<Article>>{
        return listNews
    }
}