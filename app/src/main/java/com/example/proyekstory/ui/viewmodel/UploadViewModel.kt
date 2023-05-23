package com.example.proyekstory.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyekstory.model.AddStory
import com.example.proyekstory.ui.remote.ApiConfig
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadViewModel(token: String) : ViewModel() {
    private var token = token

    private val _responseAdd = MutableLiveData<AddStory>()
    val responseAdd: LiveData<AddStory> = _responseAdd

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun uploadStory(description: RequestBody, imageMultipart: MultipartBody.Part) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().uploadStory(token, imageMultipart, description)
        client.enqueue(object : Callback<AddStory> {
            override fun onResponse(
                call: Call<AddStory>,
                response: Response<AddStory>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _responseAdd.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<AddStory>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object{
        private const val TAG = "DetailViewModel"
    }
}