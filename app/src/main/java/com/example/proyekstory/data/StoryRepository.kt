package com.example.proyekstory.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.proyekstory.database.StoryDatabase
import com.example.proyekstory.model.Story
import com.example.proyekstory.ui.remote.ApiService

class StoryRepository (
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
    private val token: String) {

    fun getStory(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
    }
}