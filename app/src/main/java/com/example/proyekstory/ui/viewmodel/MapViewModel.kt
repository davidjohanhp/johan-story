package com.example.proyekstory.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyekstory.model.StoryList
import com.example.proyekstory.ui.remote.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel(token: String) : ViewModel() {
    private var token = token

    private val _listStories = MutableLiveData<StoryList>()
    val listStories: LiveData<StoryList> = _listStories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getStoriesLocation(100)
    }

    fun getStoriesLocation(size: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getStoriesLocation(token, size)
        client.enqueue(object : Callback<StoryList> {
            override fun onResponse(
                call: Call<StoryList>,
                response: Response<StoryList>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listStories.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<StoryList>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object{
        private const val TAG = "MainViewModel"
    }
}