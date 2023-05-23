package com.example.proyekstory.ui.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.proyekstory.data.StoryRepository
import com.example.proyekstory.model.Story


class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    var story: LiveData<PagingData<Story>> =
        storyRepository.getStory().cachedIn(viewModelScope)

    fun refreshData() {
        story = storyRepository.getStory().cachedIn(viewModelScope)
    }

    companion object{
        private const val TAG = "MainViewModel"
    }
}