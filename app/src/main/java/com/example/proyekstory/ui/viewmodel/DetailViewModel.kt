package com.example.proyekstory.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyekstory.model.StoryDetail
import com.example.proyekstory.ui.remote.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(token: String) : ViewModel() {
    private var token = token

    private val _storyDetail = MutableLiveData<StoryDetail>()
    val storyDetail: LiveData<StoryDetail> = _storyDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getStory(id: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getStoryDetail(token, id)
        client.enqueue(object : Callback<StoryDetail> {
            override fun onResponse(
                call: Call<StoryDetail>,
                response: Response<StoryDetail>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _storyDetail.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<StoryDetail>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object{
        private const val TAG = "DetailViewModel"
    }
}