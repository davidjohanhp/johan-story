package com.example.proyekstory.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyekstory.data.Injection
import android.content.Context

class ViewModelFactory(private val token:String) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(token) as T
        } else if (modelClass.isAssignableFrom(UploadViewModel::class.java)) {
            return UploadViewModel(token) as T
        }
        else if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}