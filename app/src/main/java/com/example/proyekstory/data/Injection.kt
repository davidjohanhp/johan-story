package com.example.proyekstory.data

import android.content.Context
import com.example.proyekstory.database.StoryDatabase
import com.example.proyekstory.ui.remote.ApiConfig

object Injection {
    fun provideRepository(context: Context, token : String): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService, token)
    }
}